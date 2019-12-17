package com.kslimweb.ipolygot

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.search.saas.Client
import com.google.cloud.translate.Translate
import com.kslimweb.ipolygot.speechtotext.SpeechService
import com.kslimweb.ipolygot.speechtotext.VoiceRecorder
import com.kslimweb.ipolygot.util.Helper.Companion.algoliaSearchCallback
import com.kslimweb.ipolygot.util.Helper.Companion.getAlgoliaClient
import com.kslimweb.ipolygot.util.Helper.Companion.getGoogleTranslationService
import com.kslimweb.ipolygot.util.Helper.Companion.getLanguageCode
import com.kslimweb.ipolygot.util.Helper.Companion.translateText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview_speech_translate.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val REQUEST_AUDIO_PERMISSION = 200
const val STATE_RESULTS = "results"
const val ALGOLIA_INDEX_NAME = "hadith"

class MainActivity : AppCompatActivity() {

    private var mSpeechService: SpeechService? = null
    private lateinit var resultAdapter: SpeechTranslateAdapter
    private lateinit var algoliaClient: Client
    private lateinit var googleTranslateService: Translate

    private lateinit var speechLanguageCode: String
    private lateinit var translateLanguageCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        val results = savedInstanceState?.getStringArrayList(STATE_RESULTS)
        results?.let {
            resultAdapter = SpeechTranslateAdapter(it)
            rv_speech_translate_search.adapter = resultAdapter
        }

        initSpinnerItem()
        speechLanguageCode = getLanguageCode()
        translateLanguageCode = getLanguageCode()
        googleTranslateService = getGoogleTranslationService(applicationContext)
        algoliaClient = getAlgoliaClient(applicationContext)

        spinner_speech_language.setOnItemSelectedListener { view, position, id, item ->
            speechLanguageCode = getLanguageCode(position)
            startVoiceRecorder()
        }
        spinner_translate_language.setOnItemSelectedListener { view, position, id, item ->
            translateLanguageCode = getLanguageCode(position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(STATE_RESULTS, resultAdapter.getResults())
    }

    override fun onStart() {
        super.onStart()

        // Prepare Cloud Speech API
        bindService(Intent(applicationContext, SpeechService::class.java), mServiceConnection, BIND_AUTO_CREATE)

        // Start listening to voices
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder()
        }
    }

    override fun onStop() {
        // Stop listening to voice
        stopVoiceRecorder()

        // Stop Cloud Speech API
        mSpeechService?.removeListener(mSpeechServiceListener)
        unbindService(mServiceConnection)
        mSpeechService = null

        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (permissions.size == 1 && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecorder()
            } else {
                showPermissionMessageDialog()
            }
        }
    }

    private fun initSpinnerItem() {
        val adapter = ArrayAdapter.createFromResource(applicationContext, R.array.list_of_language, android.R.layout.simple_spinner_item)
        spinner_speech_language.setAdapter(adapter)
        spinner_translate_language.setAdapter(adapter)
    }

    private var mVoiceRecorder: VoiceRecorder? = null
    private val mVoiceCallback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {
            showStatus(true)
            mSpeechService?.startRecognizing(mVoiceRecorder!!.sampleRate, speechLanguageCode)
        }

        override fun onVoice(data: ByteArray, size: Int) {
            mSpeechService?.recognize(data, size)
        }

        override fun onVoiceEnd() {
            showStatus(false)
            mSpeechService?.finishRecognizing()
        }
    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
            mSpeechService = SpeechService.from(binder)
            mSpeechService?.addListener(mSpeechServiceListener)
            listening_status.visibility = View.VISIBLE
        }
        override fun onServiceDisconnected(componentName: ComponentName) {
            mSpeechService = null
        }
    }

    private fun startVoiceRecorder() {
        mVoiceRecorder?.stop()
        mVoiceRecorder = VoiceRecorder(mVoiceCallback)
        mVoiceRecorder?.start()
    }

    private fun stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder?.stop()
            mVoiceRecorder = null
        }
    }

    private val mSpeechServiceListener = SpeechService.Listener { speechText, isFinal ->
        if (isFinal) {
            mVoiceRecorder?.dismiss()
        }
        if (speech_text != null && !TextUtils.isEmpty(speechText)) {
            CoroutineScope(IO).launch {
                val translatedText=  translateText(googleTranslateService, speechText, translateLanguageCode)
                setTextOnMain(isFinal, speechText, translatedText)
            }
        }
    }

    private fun setTextOnMain(final: Boolean, speechText: String, translatedText: String) {
        CoroutineScope(Main).launch {
            withContext(Main) {
                if (final) {
                    speech_text.text = null
                    translate_text.text = null
                    if (!::resultAdapter.isInitialized) {
                        resultAdapter = SpeechTranslateAdapter()
                        rv_speech_translate_search.adapter = resultAdapter
                    }
                    algoliaSearchCallback(speechText, translatedText, resultAdapter, algoliaClient.getIndex(ALGOLIA_INDEX_NAME))
                    rv_speech_translate_search.smoothScrollToPosition(0)
                } else {
                    speech_text.text = speechText
                    translate_text.text = translatedText
                }
            }
        }
    }

    private fun showPermissionMessageDialog() {
        AlertDialog.Builder(applicationContext)
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }.create().show()
    }

    private fun showStatus(hearingVoice: Boolean) {
        runOnUiThread {
            if (hearingVoice)
                listening_status.text = "Listening..."
            else
                listening_status.text = "Not listening..."
        }
    }
}
