package com.kslimweb.ipolyglot.speechservices

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.kslimweb.ipolyglot.MainViewModel
import com.kslimweb.ipolyglot.adapter.AlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.AlgoliaSearcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.util.extension.AlQuranSearchHelper
import kotlinx.coroutines.*

class VoiceRecognizer(
//    private val mSpeechRecognizer: SpeechRecognizer,
//    private val intent: Intent,
    private val googleTranslate: GoogleTranslate,
    private val algoliaSearcher: AlgoliaSearcher,
    private val viewModel: MainViewModel,
    private val bgScope: CoroutineScope,
    private val mainDispatcher: MainCoroutineDispatcher,
    private val searchResponseAlQuranAdapter: AlQuranAdapter,
    private val alQuranSearchHelper: AlQuranSearchHelper
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onPartialResults(partialResults: Bundle?) {
        val partialSpeechText =
            partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
        bgScope.launch {
            partialSpeechText?.let {
                val translatedText = googleTranslate.translateText(
                    partialSpeechText,
                    viewModel.translateLanguageCode
                )
                viewModel.setSpeechAndTranslationText(partialSpeechText, translatedText)
                val searchHits = algoliaSearcher.search(partialSpeechText)
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

            spokenTexts?.let {
                val speechText = it[0]
                bgScope.launch {
                    val alQuranSearchHits = algoliaSearcher.search(speechText)
                    val translatedText = googleTranslate.translateText(speechText, viewModel.translateLanguageCode)
                    val storedAlQuran = alQuranSearchHelper.getStoredAlQuranResult()

                    // means is at first start of reciting
                    if (speechText.contains("اللّه اكْبر") || speechText.contains("بسم الله الرحمان الرحيم")) {
                        // this can be either at start or at stop current chapter
                        alQuranSearchHelper.clearAlQuran()
                    }
                    else if (storedAlQuran == null) {
                        if (alQuranSearchHits.isNotEmpty()) {
                            alQuranSearchHelper.storeCurrentChapterVerses(alQuranSearchHits[0].chapter.toString())
                            alQuranSearchHelper.bufferChapterVerses(alQuranSearchHits[0])
                        }
                    } else {
                        val bufferedAlQuran = alQuranSearchHelper.getBufferedAlQuran()
                        // TODO compare buffered result of n, n+1, n-1, n-2
                        // TODO no need store in sp. Just save it in memory
                        // compare with speech text, if the whole string is match, retrieve it
                        // if is forward, take verse+1 in the stored SP and store into buffer.
                    }

                    viewModel.setSpeechAndTranslationText(speechText, translatedText)
                    setRecyclerViewSearchData(alQuranSearchHits)
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
