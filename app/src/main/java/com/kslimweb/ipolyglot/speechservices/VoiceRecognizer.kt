package com.kslimweb.ipolyglot.speechservices

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.algolia.search.saas.Client
import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.ALGOLIA_INDEX_NAME
import com.kslimweb.ipolyglot.MainActivity.Companion.translateLanguageCode
import com.kslimweb.ipolyglot.TRANSLATE_MODEL
import com.kslimweb.ipolyglot.model.Hit
import com.kslimweb.ipolyglot.network.algolia.Algolia
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.ui.SpeechTranslateAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VoiceRecognizer(
    private val mSpeechRecognizer: SpeechRecognizer,
    private val intent: Intent,
    private val activity: Activity,
    private val signal: VoiceRecognizerInterface,
    private val googleTranslateClient: Translate,
    private val algoliaClient: Client
) : RecognitionListener {
    
    private lateinit var speechTranslateAdapter: SpeechTranslateAdapter

    override fun onReadyForSpeech(params: Bundle?) { }

    override fun onRmsChanged(rmsdB: Float) { }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onPartialResults(partialResults: Bundle?) { }

    override fun onEvent(eventType: Int, params: Bundle?) { }

    override fun onBeginningOfSpeech() { }

    override fun onEndOfSpeech() { }

    override fun onError(error: Int) { }

    override fun onResults(results: Bundle?) {
        if (results != null) {
            val spokenTexts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            spokenTexts?.let {
                val speechText = it[0]
                signal.spokenText(speechText)
                CoroutineScope(Dispatchers.IO).launch {
                    val translatedText = GoogleTranslate(googleTranslateClient).translateText(speechText, translateLanguageCode, TRANSLATE_MODEL)
                    val finalList = Algolia().algoliaSearch(speechText, translatedText, algoliaClient.getIndex(
                        ALGOLIA_INDEX_NAME
                    ))
                   setAdapter(speechText, translatedText, finalList)
                }
            }
        }

        // TODO auto listen again add delay
//        mSpeechRecognizer.startListening(intent)
        activity.speech_to_text_button.text = "Start Speech to Text"
    }

    private suspend fun setAdapter(speechText: String, translatedText: String, finalList: List<Hit>) {
        withContext(Dispatchers.Main) {
            if (!::speechTranslateAdapter.isInitialized) {
                speechTranslateAdapter = SpeechTranslateAdapter(speechText, translatedText, finalList)
                activity.rv_speech_translate_search.adapter = speechTranslateAdapter
            } else {
                speechTranslateAdapter.setResult(speechText, translatedText, finalList)
            }
        }
    }
}