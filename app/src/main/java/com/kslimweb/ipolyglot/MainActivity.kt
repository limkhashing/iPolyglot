package com.kslimweb.ipolyglot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.algolia.search.saas.Client
import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.algolia_data.Hit
import com.kslimweb.ipolyglot.util.Helper
import com.kslimweb.ipolyglot.util.Helper.Companion.getAlgoliaClient
import com.kslimweb.ipolyglot.util.Helper.Companion.getGoogleTranslationService
import com.kslimweb.ipolyglot.util.Helper.Companion.getLanguageCode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val REQUEST_AUDIO_PERMISSION = 200
const val ALGOLIA_INDEX_NAME = "hadith"
const val TRANSLATE_MODEL = "base"
const val REQ_CODE_SPEECH_INPUT = 100

class MainActivity : AppCompatActivity() {

    private lateinit var speechTranslateAdapter: SpeechTranslateAdapter
    private lateinit var algoliaClient: Client
    private lateinit var googleTranslateService: Translate

    private lateinit var speechLanguageCode: String
    private lateinit var translateLanguageCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        initSpinnerItem()
        speechLanguageCode = getLanguageCode()
        translateLanguageCode = getLanguageCode()
        googleTranslateService = getGoogleTranslationService(applicationContext)
        algoliaClient = getAlgoliaClient(applicationContext)

        speech_to_text_button.setOnClickListener {
            inputSpeech()
        }

        // TODO use google intent
        //  implement stop feature and clear the input text
        //  allow type
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
            if (permissions.size == 1 && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startVoiceRecorder()
            } else {
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

    private fun inputSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            speechLanguageCode
        )
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {

                val speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)[0]

                speechText?.let {
                    CoroutineScope(IO).launch {
                        val translatedText = Helper.translateText(googleTranslateService, speechText, translateLanguageCode, TRANSLATE_MODEL)
                        val finalList = Helper.algoliaSearch(speechText, translatedText, algoliaClient.getIndex(ALGOLIA_INDEX_NAME))
                        setAdapter(speechText, translatedText, finalList)
                    }
                }
            }
        }

    }

    private suspend fun setAdapter(speechText: String, translatedText: String, finalList: List<Hit>) {
        withContext(Main) {
            if (!::speechTranslateAdapter.isInitialized) {
                speechTranslateAdapter = SpeechTranslateAdapter(speechText, translatedText, finalList)
                rv_speech_translate_search.adapter = speechTranslateAdapter
            } else {
                speechTranslateAdapter.setResult(speechText, translatedText, finalList)
            }
        }
    }
}
