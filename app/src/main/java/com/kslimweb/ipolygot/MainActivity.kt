package com.kslimweb.ipolygot

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_result.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import java.io.IOException

const val REQUEST_AUDIO_PERMISSION = 200
const val STATE_RESULTS = "results"
private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity(){

    private var mSpeechService: SpeechService? = null
    private var mAdapter: ResultAdapter? = null
    private var speechLanguageCode: String? = null
    private var translateLanguageCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        val results = savedInstanceState?.getStringArrayList(STATE_RESULTS)
        mAdapter = ResultAdapter(results)
        recycler_view.adapter = mAdapter

        initSpinnerItem()

        spinner_speech_language.setOnItemSelectedListener { view, position, id, item ->
            speechLanguageCode = getSelectedSpeechLanguage()
        }

        spinner_translate_language.setOnItemSelectedListener { view, position, id, item ->
            translateLanguageCode = getSelectedTranslateLanguage()
        }
    }

    private var mVoiceRecorder: VoiceRecorder? = null
    private val mVoiceCallback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {
            showStatus(true)
            mSpeechService?.startRecognizing(mVoiceRecorder!!.sampleRate,
                speechLanguageCode)
        }

        override fun onVoice(data: ByteArray, size: Int) {
            mSpeechService?.recognize(data, size)
        }

        override fun onVoiceEnd() {
            showStatus(false)
            mSpeechService?.finishRecognizing()
        }
    }

    private fun showStatus(hearingVoice: Boolean) {
        runOnUiThread {
            if (hearingVoice)
                listening_status.text = "Listening..."
            else
                listening_status.text = "Not listening..."
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

    private val mSpeechServiceListener = SpeechService.Listener { text, isFinal ->
            if (isFinal) {
                mVoiceRecorder?.dismiss()
            }
            if (speech_text != null && !TextUtils.isEmpty(text)) {
                runOnUiThread {
                    if (isFinal) {
                        speech_text.text = null
                        translate_text.text = null
                        mAdapter?.addResult(text, translateText(text)!!)
                        recycler_view.smoothScrollToPosition(0)
                    } else {
                        speech_text.text = text
                        translate_text.text = translateText(text)
                    }
                }
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mAdapter != null) {
            outState.putStringArrayList(STATE_RESULTS, mAdapter?.getResults())
        }
    }

    override fun onStart() {
        super.onStart()

        // Prepare Cloud Speech API
        bindService(Intent(this, SpeechService::class.java), mServiceConnection, BIND_AUTO_CREATE)

        // Start listening to voices
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
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

    private fun showPermissionMessageDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }.create().show()
    }

    private fun translateText(text: String): String? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            resources.openRawResource(R.raw.credential).use { `is` ->
                val myCredentials = GoogleCredentials.fromStream(`is`)
                val translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                val translate = translateOptions.service
                val translation = translate.translate(text,
                    Translate.TranslateOption.targetLanguage(translateLanguageCode),
                    // Use "base" for standard edition, "nmt" for the premium model.
                    Translate.TranslateOption.model("base"))
                val translatedText = translation.translatedText
                Log.d(TAG, "Translated Text: " + translatedText)
                return translatedText
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
        return null
    }

    private fun initSpinnerItem() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.list_of_language, android.R.layout.simple_spinner_item)
        spinner_speech_language.setAdapter(adapter)
        spinner_translate_language.setAdapter(adapter)
    }

    private fun getSelectedSpeechLanguage(): String {
        val languagePosition = spinner_speech_language.selectedIndex
        var languageCode = ""
        when (languagePosition) {
            0 -> languageCode = "ar"
            1 -> languageCode = "ms"
            2 -> languageCode = "zh"
            3 -> languageCode = "en"
            4 -> languageCode = "fr"
            5 -> languageCode = "de"
            6 -> languageCode = "hi"
            7 -> languageCode = "it"
            8-> languageCode = "ja"
            9 -> languageCode = "ko"
            10 -> languageCode = "pa"
            11 -> languageCode = "ta"
            12 -> languageCode = "tl"
            13 -> languageCode = "th"
            14 -> languageCode = "vi"
        }
        Log.d(TAG, "getSelectedSpeechLanguage: $languageCode")
        return languageCode
    }

    private fun getSelectedTranslateLanguage(): String {
        val languagePosition = spinner_translate_language.selectedIndex
        var languageCode = ""
        when (languagePosition) {
            0 -> languageCode = "ar"
            1 -> languageCode = "ms"
            2 -> languageCode = "zh"
            3 -> languageCode = "en"
            4 -> languageCode = "fr"
            5 -> languageCode = "de"
            6 -> languageCode = "hi"
            7 -> languageCode = "it"
            8-> languageCode = "ja"
            9 -> languageCode = "ko"
            10 -> languageCode = "pa"
            11 -> languageCode = "ta"
            12 -> languageCode = "tl"
            13 -> languageCode = "th"
            14 -> languageCode = "vi"
        }
        Log.d(TAG, "getSelectedTranslateLanguage: $languageCode")
        return languageCode
    }
}
