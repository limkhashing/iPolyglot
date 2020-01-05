package com.kslimweb.ipolyglot.speechservices

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import com.algolia.search.client.Index
import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.MainActivity.Companion.isSpeaking
import com.kslimweb.ipolyglot.MainActivity.Companion.translateLanguageCode
import com.kslimweb.ipolyglot.model.hit.Hit
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.ui.SpeechTranslateAdapter
import com.kslimweb.ipolyglot.util.AppConstants.TRANSLATE_MODEL
import com.kslimweb.ipolyglot.util.AppConstants.listeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.notListeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStart
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
//            val confidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
//            Log.d("Results", spokenTexts?.toString() + " abc")
//            Log.d("Results", confidences?.contentToString() + " abc")
            spokenTexts?.let {
                val speechText = it[0]
                CoroutineScope(Dispatchers.IO).launch {
                    val translatedText = GoogleTranslate(googleTranslateClient).translateText(speechText, translateLanguageCode, TRANSLATE_MODEL)
                    val finalList = Searcher(speechText, translatedText, index).search()
                    setAdapter(speechText, translatedText, finalList)
                }
            }
        }

        mSpeechRecognizer.stopListening()
//        Handler().postDelayed({ mSpeechRecognizeening(intent) }, 3000)
        setMainUI(speechToTextButtonTextStart, notListeningStatus, false)
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