package com.kslimweb.ipolyglot.speechservices

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.algolia.search.client.Index
import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.MainActivity.Companion.isSpeaking
import com.kslimweb.ipolyglot.MainActivity.Companion.translateLanguageCode
import com.kslimweb.ipolyglot.TRANSLATE_MODEL
import com.kslimweb.ipolyglot.model.hit.Hit
import com.kslimweb.ipolyglot.network.algolia.Algolia
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.ui.SpeechTranslateAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val speechToTextButtonTextStart = "Start Speech to Text"
const val notListeningStatus = "Not Listening..."

const val speechToTextButtonTextStop = "Stop Speech to Text"
const val listeningStatus = "Listening..."

class VoiceRecognizer(
    private val mSpeechRecognizer: SpeechRecognizer,
    private val intent: Intent,
    private val activity: Activity,
    private val googleTranslateClient: Translate,
    private val index: Index
) : RecognitionListener {

    private lateinit var speechTranslateAdapter: SpeechTranslateAdapter

    override fun onReadyForSpeech(params: Bundle?) { setMainUI(speechToTextButtonTextStop, listeningStatus, true) }

    override fun onRmsChanged(rmsdB: Float) { }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) { }

    override fun onBeginningOfSpeech() { }

    override fun onEndOfSpeech() { }

    override fun onError(error: Int) {
        setMainUI(speechToTextButtonTextStart, notListeningStatus, false)
    }

    override fun onResults(results: Bundle?) {
        if (results != null) {
            val spokenTexts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            spokenTexts?.let {
                val speechText = it[0]
                CoroutineScope(Dispatchers.IO).launch {
                    val translatedText = GoogleTranslate(googleTranslateClient).translateText(speechText, translateLanguageCode, TRANSLATE_MODEL)
                    val finalList = Algolia(speechText, translatedText, index).algoliaSearch()
                    setAdapter(speechText, translatedText, finalList)
                }
            }
        }

        // TODO play around deduplication
        mSpeechRecognizer.stopListening()
        Handler().postDelayed({ mSpeechRecognizer.startListening(intent) }, 3000)
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

    private fun setMainUI(speechToTextButtonText: String, listeningStatus: String, speak: Boolean) {
        activity.speech_to_text_button.text = speechToTextButtonText
        activity.listening_status.text =  listeningStatus
        isSpeaking = speak
    }
}