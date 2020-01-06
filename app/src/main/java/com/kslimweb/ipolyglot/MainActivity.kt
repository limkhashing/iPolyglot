package com.kslimweb.ipolyglot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizer
import com.kslimweb.ipolyglot.util.AppConstants.REQUEST_AUDIO_PERMISSION
import com.kslimweb.ipolyglot.util.AppConstants.listeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.notListeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStart
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var translateLanguageCode: String
        var isSpeaking = false
    }

    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private lateinit var speechLanguageCode: String

    @Inject lateinit var googleTranslate: GoogleTranslate
    @Inject lateinit var searcher: Searcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        val component = (application as BaseApplication).getAppComponent()
            .getActivityComponentFactory()
            .create()
        component.injectActivity(this)

        initSpinnerItem()
        speechLanguageCode = getLanguageCode()
        translateLanguageCode = getLanguageCode()

        initSpeechRecognizerListener()

        spinner_speech_language.setOnItemSelectedListener { view, position, id, item ->
            speechLanguageCode = getLanguageCode(position)
//            if (isSpeaking) {
//                setSpeechRecognizerListener()
//                mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
//            }
        }
        spinner_translate_language.setOnItemSelectedListener { view, position, id, item ->
            translateLanguageCode = getLanguageCode(position)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (permissions.size != 1 && grantResults.size != 1 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
             showPermissionMessageDialog()
            }
        }
    }

    private fun initSpinnerItem() {
        val adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.list_of_language,
            android.R.layout.simple_spinner_item
        )
        spinner_speech_language.setAdapter(adapter)
        spinner_translate_language.setAdapter(adapter)
    }

    private fun showPermissionMessageDialog() {
        AlertDialog.Builder(applicationContext)
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }.create().show()
    }

    private fun getSpeechRecognizeIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        //Customize language by passing language code
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLanguageCode)
        if (speechLanguageCode == "ar") {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-IL")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-JO")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-AE")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-BH")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-DZ")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-IQ")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-KW")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-MA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-TN")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-OM")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-PS")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-QA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-LB")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG")
        }

        //To receive partial results on the callback
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        return intent
    }

    fun inputSpeech(view: View) {
        if (!isSpeaking) {
//            setSpeechRecognizerListener()
            mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
            speech_to_text_button.text = speechToTextButtonTextStop
            listening_status.text = listeningStatus
            isSpeaking = true
            spinner_speech_language.isEnabled = false
            spinner_translate_language.isEnabled = false
        } else {
            mSpeechRecognizer.cancel()
            speech_to_text_button.text = speechToTextButtonTextStart
            listening_status.text = notListeningStatus
            isSpeaking = false
            spinner_speech_language.isEnabled = true
            spinner_translate_language.isEnabled = true
        }
    }

    private fun initSpeechRecognizerListener() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        mSpeechRecognizer.setRecognitionListener(VoiceRecognizer(mSpeechRecognizer,
//            getSpeechRecognizeIntent(),
            this@MainActivity,
            googleTranslate,
            searcher))
    }

    override fun onStop() {
        super.onStop()
        mSpeechRecognizer.stopListening()
        mSpeechRecognizer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSpeechRecognizer.destroy()
    }

    private fun getLanguageCode(position: Int = 0): String {
        var languageCode = ""
        when (position) {
            0 -> languageCode = "ar"
            1 -> languageCode = "ms"
            2 -> languageCode = "zh"
            3 -> languageCode = "en"
            4 -> languageCode = "fr"
            5 -> languageCode = "de"
            6 -> languageCode = "hi"
            7 -> languageCode = "it"
            8 -> languageCode = "ja"
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
