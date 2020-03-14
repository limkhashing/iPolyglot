package com.kslimweb.ipolyglot.speechservices

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.kslimweb.ipolyglot.MainViewModel
import com.kslimweb.ipolyglot.adapter.AlQuranAdapter
import com.kslimweb.ipolyglot.model.alquran.HitAlQuran
import com.kslimweb.ipolyglot.network.algolia.AlgoliaSearcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import kotlinx.coroutines.*
import java.util.*

class VoiceRecognizer(
//    private val mSpeechRecognizer: SpeechRecognizer,
//    private val intent: Intent,
    private val googleTranslate: GoogleTranslate,
    private val algoliaSearcher: AlgoliaSearcher,
    private val viewModel: MainViewModel,
    private val bgScope: CoroutineScope,
    private val mainDispatcher: MainCoroutineDispatcher,
    private val searchResponseAlQuranAdapter: AlQuranAdapter
) : RecognitionListener {

    private lateinit var bgJob: Job

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onPartialResults(partialResults: Bundle?) {
        val partialSpeechText = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
        bgJob = bgScope.launch {
            partialSpeechText?.let {
                val translatedText = googleTranslate.translateText(
                    partialSpeechText,
                    viewModel.translateLanguageCode
                )
                viewModel.setSpeechAndTranslationText(partialSpeechText, translatedText)
                setRecyclerViewSearchData(algoliaSearcher.search(partialSpeechText))
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
                    bgJob.cancelAndJoin()

                    val alQuranSearchHits = algoliaSearcher.search(speechText)
                    val translatedText = googleTranslate.translateText(speechText, viewModel.translateLanguageCode)
                    viewModel.setSpeechAndTranslationText(speechText, translatedText)

                    if (speechText.contains("اللّه اكْبر") || speechText.contains("بسم الله الرحمان الرحيم")
                        || speechText.toUpperCase(Locale.getDefault()).contains("ALLAHU AKBAR")) {
                        setRecyclerViewSearchData(emptyList())
                    } else {
                        setRecyclerViewSearchData(alQuranSearchHits)
                    }

//                    val storedAlQuran = alQuranSearchHelper.getCurrentChapterVerses()
//                    // can be either at start or at stop current chapter
//                    if (speechText.contains("اللّه اكْبر") || speechText.contains("بسم الله الرحمان الرحيم")
//                        || speechText.toUpperCase(Locale.getDefault()).contains("ALLAHU AKBAR")) {
//                        alQuranSearchHelper.clearAlQuran()
//                        isFreshStart = true
//
//                    } else if (storedAlQuran.isEmpty()) {
//                        if (alQuranSearchHits.isNotEmpty()) {
//                            alQuranSearchHelper.storeCurrentChapterVerses(alQuranSearchHits[0].chapter.toString())
//                            alQuranSearchHelper.setBufferAlQuran(alQuranSearchHits[0])
//                        }
//
//                    } else {
//                        val bufferedAlQuran = alQuranSearchHelper.getBufferedAlQuran()
//                        val nextAlQuran = bufferedAlQuran[bufferedAlQuran.size - 1]
//
//                        if (alQuranSearchHits.isNotEmpty()) {
//                            // means i know reciter is reciting the next verse, buffer the next incoming verse
//                            if (alQuranSearchHits[0].verse == nextAlQuran.verse) {
//                                alQuranSearchHelper.addBufferedAlQuran()
//                            }
//                            viewModel.setSpeechAndTranslationText(alQuranSearchHits[0].meaning, alQuranSearchHits[0].translation)
//                        }

//                        bufferedAlQuran.forEachIndexed { index, it ->
//                            var count = 0
//                            speechText.forEach { char ->
//                                it.meaning.forEach { meaningChar ->
//                                    if (char == meaningChar) count++
//                                }
//                                it.translation.forEach { meaningChar ->
//                                    if (char == meaningChar) count++
//                                }
//                            }
//                            if (count >= 4) {
//                                viewModel.setSpeechAndTranslationText(it.meaning, it.translation)
//                                if (index == bufferedAlQuran.lastIndex) {
//                                    alQuranSearchHelper.addBufferedAlQuran()
//                                }
//                            }
//                        }
//                        isFreshStart = false
//                    }

//                    if (isFreshStart)
//                        viewModel.setSpeechAndTranslationText(speechText, translatedText)
//                    setRecyclerViewSearchData(alQuranSearchHits)
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
