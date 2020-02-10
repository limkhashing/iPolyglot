package com.kslimweb.ipolyglot.speechservices

import android.app.Activity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.kslimweb.ipolyglot.MainViewModel
import com.kslimweb.ipolyglot.model.hadith.HitHadith
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.ui.SpeechTranslateAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoiceRecognizer(
    private val mSpeechRecognizer: SpeechRecognizer,
//    private val intent: Intent,
    private val activity: Activity,
    private val googleTranslate: GoogleTranslate,
    private val searcher: Searcher,
    private val viewModel: MainViewModel
) : RecognitionListener {

    private lateinit var speechTranslateAdapter: SpeechTranslateAdapter

    override fun onReadyForSpeech(params: Bundle?) { }

    override fun onRmsChanged(rmsdB: Float) { }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) { }

    override fun onBeginningOfSpeech() { }

    override fun onEndOfSpeech() { }

    override fun onError(error: Int) {
        mSpeechRecognizer.cancel()
        viewModel.onVoiceFinished(true)
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
                    val translatedText = googleTranslate.translateText(speechText, viewModel.translateLanguageCode)
                    val searchHits = searcher.search(speechText, translatedText)
                    setAdapter(speechText, translatedText, searchHits)
                }
            }
        }

//        Handler().postDelayed({ mSpeechRecognizeening(intent) }, 3000)
        viewModel.onVoiceFinished()
    }

    private suspend fun setAdapter(speechText: String, translatedText: String, finalList: List<HitHadith>) {
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