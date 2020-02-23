package com.kslimweb.ipolyglot.speechservices

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kslimweb.ipolyglot.MainViewModel
import com.kslimweb.ipolyglot.adapter.SearchResponseAlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import kotlinx.coroutines.*

class VoiceRecognizer(
    private val mSpeechRecognizer: SpeechRecognizer,
//    private val intent: Intent,
    private val googleTranslate: GoogleTranslate,
    private val searcher: Searcher,
    private val viewModel: MainViewModel,
    private val gson: Gson,
    private val rvSearch: RecyclerView,
    private val bgScope: CoroutineScope,
    private val mainDispatcher: MainCoroutineDispatcher,
    private val searchResponseAlQuranAdapter: SearchResponseAlQuranAdapter
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) { }

    override fun onRmsChanged(rmsdB: Float) { }

    override fun onBufferReceived(buffer: ByteArray?) { }

    override fun onPartialResults(partialResults: Bundle?) {
        val partialSpeechText = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
        bgScope.launch {
            partialSpeechText?.let {
                val translatedText = googleTranslate.translateText(partialSpeechText, viewModel.translateLanguageCode)
                viewModel.setSpeechAndTranslationText(partialSpeechText, translatedText)
                val searchHits = searcher.search(partialSpeechText, translatedText)
                setRecyclerViewSearchData(searchHits)
            }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) { }

    override fun onBeginningOfSpeech() { }

    override fun onEndOfSpeech() { }

    override fun onError(error: Int) {
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
                bgScope.launch {
                    val translatedText = googleTranslate.translateText(speechText, viewModel.translateLanguageCode)
                    val searchHits = searcher.search(speechText, translatedText)
                    viewModel.setSpeechAndTranslationText(speechText, translatedText)
                    setRecyclerViewSearchData(searchHits)
                }
            }
        }

//        Handler().postDelayed({ mSpeechRecognizeening(intent) }, 3000)
        viewModel.onVoiceFinished()
    }

    private suspend fun setRecyclerViewSearchData(searchHits: List<HitAlQuran>) {
        withContext(mainDispatcher) {
            if (searchHits.isNotEmpty()) {
                viewModel.appearInLabelText.set("Appear In: ")
                viewModel.searchRecyclerViewVisibility.set(true)
                searchResponseAlQuranAdapter.setData(searchHits)
            } else {
                viewModel.appearInLabelText.set("Appear In: None")
                viewModel.searchRecyclerViewVisibility.set(false)
            }
        }
    }
}
