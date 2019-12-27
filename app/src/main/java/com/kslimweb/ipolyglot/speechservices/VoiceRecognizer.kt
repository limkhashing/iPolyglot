package com.kslimweb.ipolyglot.speechservices

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.google.cloud.translate.Translate
import com.google.gson.Gson
import com.kslimweb.ipolyglot.ALGOLIA_INDEX_NAME
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
    private val signal: VoiceRecognizerInterface,
    private val googleTranslateClient: Translate,
    private val algoliaClient: ClientSearch
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
                signal.spokenText(speechText)
                CoroutineScope(Dispatchers.IO).launch {
                    val translatedText = GoogleTranslate(googleTranslateClient).translateText(speechText, translateLanguageCode, TRANSLATE_MODEL)

                    val index = algoliaClient.initIndex(IndexName(ALGOLIA_INDEX_NAME))
                    val searcher = SearcherSingleIndex(index, Query(query = speechText, hitsPerPage = 20))
                    val searchResult = searcher.search()

//                    val finalList = Algolia().algoliaSearch(speechText, translatedText, index)
//                    setAdapter(speechText, translatedText, finalList)
                }
            }
        }

        // TODO play around advanced syntax, deduplication
        //  fix when changing input speech language during speaking
        mSpeechRecognizer.stopListening()
        Handler().postDelayed({
            mSpeechRecognizer.startListening(intent)
        }, 2000)
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