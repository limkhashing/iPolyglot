package com.kslimweb.ipolygot

import android.Manifest
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_result.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import java.util.ArrayList

const val REQUEST_AUDIO_PERMISSION = 200
const val STATE_RESULTS = "results"
const val FRAGMENT_MESSAGE_DIALOG = "message_dialog"
const val inputSpeechLanguageCode = "ar"
const val projectId = "ipolygot"
const val projectLocation = "global"
private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {

    private var mSpeechService: SpeechService? = null
    private var mAdapter: ResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_AUDIO_PERMISSION)

        val results = savedInstanceState?.getStringArrayList(STATE_RESULTS)
        mAdapter = ResultAdapter(results)
        recycler_view.adapter = mAdapter

        initSpinnerItem()

//        input_speech_spinner.setOnItemSelectedListener { view, position, id, item ->
//            translateText(projectId,
//                projectLocation,
//                inputSpeechLanguageCode,
//                getSpinnerSelectedLanguage())
//        }
    }

    private var mVoiceRecorder: VoiceRecorder? = null
    private val mVoiceCallback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {
            showStatus(true)

            mSpeechService?.startRecognizing(mVoiceRecorder!!.sampleRate,
                getSpinnerSelectedInputSpeechLanguage())
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
                        mAdapter?.addResult(text)
                        //TODO translation
                        Log.d(TAG, text)
                        recycler_view.smoothScrollToPosition(0)
                    } else {
                        speech_text.text = text
                        Log.d(TAG, text)
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

//    private fun translateText(projectId: String,
//                              location: String,
//                              sourceLanguageCode: String,
//                              targetLanguageCode: String) {
//
//        val credentials = GoogleCredentials.fromStream(assets.open("credentials.json"))
//        val credentialsProvider = FixedCredentialsProvider.create(credentials)
//        val translationServiceSettings = TranslationServiceSettings
//            .newBuilder()
//            .setCredentialsProvider(credentialsProvider)
//            .build()
//        val translationServiceClient = TranslationServiceClient.create(translationServiceSettings)
//        val locationName = LocationName.newBuilder()
//            .setProject(projectId)
//            .setLocation(location).build()
//
//        val translateTextRequest =
//            TranslateTextRequest.newBuilder()
//                .setParent(locationName.toString())
//                .setMimeType("text/plain")
//                .setSourceLanguageCode(sourceLanguageCode)
//                .setTargetLanguageCode(targetLanguageCode)
//                .addContents(textview_speech_to_text.text.toString())
//                .build()
//        // Call the API
//        val response = translationServiceClient.translateText(translateTextRequest)
//        textview_translated_text.text = response.translationsList[0].translatedText
//    }

//    private fun getTranslateService(text: String) {
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
//        try {
//            resources.openRawResource(R.raw.credential).use { `is` ->
//                val myCredentials = GoogleCredentials.fromStream(`is`)
//                val translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build()
//                val translate = translateOptions.service
//                //Get input text to be translated:
//                val translation = translate.translate(text, Translate.TranslateOption.targetLanguage(getSpinnerSelectedLanguage()), Translate.TranslateOption.model("base"))
//                val translatedText = translation.translatedText
//
//                //Translated text and original text are set to TextViews:
////                textview_translated_text.text = translatedText
//            }
//        } catch (ioe: IOException) {
//            ioe.printStackTrace()
//        }
//    }

    private fun initSpinnerItem() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.list_of_language, android.R.layout.simple_spinner_item)
        input_speech_spinner.setAdapter(adapter)
    }

    private fun getSpinnerSelectedInputSpeechLanguage(): String {
        val languagePosition = input_speech_spinner.selectedIndex
        Log.d(TAG, "getSpinnerSelectedLanguage Position: $languagePosition")
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
        return languageCode
    }
}
