package com.kslimweb.ipolyglot.speechservices

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.kslimweb.ipolyglot.MainViewModel
import com.kslimweb.ipolyglot.adapter.AlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.util.extension.SearchResultHelper
import kotlinx.coroutines.*

class VoiceRecognizer(
//    private val mSpeechRecognizer: SpeechRecognizer,
//    private val intent: Intent,
    private val googleTranslate: GoogleTranslate,
    private val searcher: Searcher,
    private val viewModel: MainViewModel,
    private val bgScope: CoroutineScope,
    private val mainDispatcher: MainCoroutineDispatcher,
    private val searchResponseAlQuranAdapter: AlQuranAdapter,
    private val searchResultHelper: SearchResultHelper
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onPartialResults(partialResults: Bundle?) {
        val partialSpeechText = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
        bgScope.launch {
            partialSpeechText?.let {
                val translatedText = googleTranslate.translateText(partialSpeechText, viewModel.translateLanguageCode)
                viewModel.setSpeechAndTranslationText(partialSpeechText, translatedText)
                val searchHits = searcher.search(partialSpeechText)
                setRecyclerViewSearchData(searchHits)
            }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {}

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
                    val alQuranSearchHits = searcher.search(speechText)
                    val translatedText = googleTranslate.translateText(speechText, viewModel.translateLanguageCode)
                    val storedResult = searchResultHelper.getStoredSearchResult()


                    if (speechText.contains("اللّه اكْبر")) {
                        // clear stored search result, search and store again
                        searchResultHelper.clearStoredResult()

                    } else if (storedResult != null) {
                        // TODO use stored result

                    } else if (alQuranSearchHits.isNotEmpty()) {
                        storeCurrentChapterVerses(alQuranSearchHits[0].chapter.toString())
                    }

                    viewModel.setSpeechAndTranslationText(speechText, translatedText)
                    setRecyclerViewSearchData(alQuranSearchHits)
                }
            }
        }

//        Handler().postDelayed({ mSpeechRecognizeening(intent) }, 3000)
        viewModel.onVoiceFinished()
    }

    private suspend fun storeCurrentChapterVerses(chapter: String) =
        searchResultHelper.storeSearchResultObject(searcher.searchVersesInChapter(chapter))

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
