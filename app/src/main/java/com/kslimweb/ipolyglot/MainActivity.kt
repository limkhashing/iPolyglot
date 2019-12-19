package com.kslimweb.ipolyglot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.algolia.search.saas.Client
import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizer
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizerInterface
import com.kslimweb.ipolyglot.ui.SpeechTranslateAdapter
import com.kslimweb.ipolyglot.util.CredentialsHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview_speech_translate.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

const val REQUEST_AUDIO_PERMISSION = 200
const val ALGOLIA_INDEX_NAME = "hadith"
const val TRANSLATE_MODEL = "base"

class MainActivity : AppCompatActivity(), VoiceRecognizerInterface {

    companion object {
        lateinit var speechLanguageCode: String
        lateinit var translateLanguageCode: String
    }

    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private lateinit var algoliaClient: Client
    private lateinit var googleTranslateService: Translate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        initSpinnerItem()
        speechLanguageCode = getLanguageCode()
        translateLanguageCode = getLanguageCode()

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(applicationContext)
        }

        googleTranslateService = CredentialsHelper(applicationContext).initGoogleTranslateClient()
        algoliaClient = CredentialsHelper(applicationContext).initAlgoliaClient()

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        mSpeechRecognizer.setRecognitionListener(VoiceRecognizer(mSpeechRecognizer,
            getSpeechRecognizeIntent(),
            this,
            this@MainActivity,
            googleTranslateService,
            algoliaClient))

        spinner_speech_language.setOnItemSelectedListener { view, position, id, item ->
            speechLanguageCode = getLanguageCode(position)
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

        //To receive partial results on the callback
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        return intent
    }

    fun inputSpeech(view: View) {
        speech_to_text_button.text = "Stop Speech to Text"
        mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
    }

    override fun spokenText(spokenText: String) {
        Log.d("Main", spokenText)
    }

    override fun onStop() {
        super.onStop()
        mSpeechRecognizer.cancel()
        mSpeechRecognizer.stopListening()
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
