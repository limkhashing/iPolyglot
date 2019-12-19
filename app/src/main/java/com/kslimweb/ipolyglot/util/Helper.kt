package com.kslimweb.ipolyglot.util

import android.content.Context
import com.algolia.search.saas.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.gson.Gson
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.algolia_data.AlgorliaCredentials
import com.kslimweb.ipolyglot.algolia_data.Hit
import com.kslimweb.ipolyglot.algolia_data.HitsJson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONObject

class Helper {
    companion object {
        fun getLanguageCode(position: Int = 0): String {
            var languageCode = ""
            when (position) {
                0 -> languageCode = "ar"
                1 -> languageCode = "ms"
                2 -> languageCode = "zh"
                3 -> languageCode = "en"
                4 -> languageCode = "fr"
                5 -> languageCode = "de"
                6 -> languageCode = "hi"
                7 -> languageCode = "it"
                8 -> languageCode = "ja"
                9 -> languageCode = "ko"
                10 -> languageCode = "pa"
                11 -> languageCode = "ta"
                12 -> languageCode = "tl"
                13 -> languageCode = "th"
                14 -> languageCode = "vi"
            }
            return languageCode
        }

        fun getAlgoliaClient(context: Context): Client {
            val algoliaJson = context.resources.openRawResource(R.raw.algolia_dev).bufferedReader().use { it.readText() }
            val algoliaCredentials = Gson().fromJson(algoliaJson, AlgorliaCredentials::class.java)
            return Client(algoliaCredentials.appId, algoliaCredentials.apiSearchKey)
        }

        fun getGoogleTranslationService(context: Context): Translate {
            context.resources.openRawResource(R.raw.credential_dev).use { `is` ->
                val myCredentials = GoogleCredentials.fromStream(`is`)
                val translateOptions =
                    TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                return translateOptions.service
            }
        }

        suspend fun translateText(translate: Translate, text: String, translateLanguageCode: String, model: String): String {
            var translatedText = ""
            withContext(IO) {
                translatedText = translate.translate(
                    text,
                    Translate.TranslateOption.targetLanguage(translateLanguageCode),
                    // Use "base" for standard edition, "nmt" for the premium model.
                    Translate.TranslateOption.model(model)
                ).translatedText
            }
            return translatedText
        }

        private fun speechTextSearch(speechText: String, index: Index): JSONObject? {
            return index.search(Query(speechText), null)
        }

        private fun translatedTextSearch(translatedText: String, index: Index): JSONObject? {
            return index.search(Query(translatedText), null)
        }

        suspend fun algoliaIndexSearch(speechText: String, translatedText: String, index: Index): List<Hit> {
            var finalList = emptyList<Hit>()
            withContext(IO) {
                val speechTextSearchJson = async {
                    speechTextSearch(speechText, index)
                }
                val translatedTextSearchJson = async {
                    translatedTextSearch(translatedText, index)
                }
                finalList = pasrseSearchList(speechTextSearchJson.await(), translatedTextSearchJson.await())
            }
            return finalList
        }

        private fun pasrseSearchList(speechTextSearchJson: JSONObject?, translatedTextSearchJson: JSONObject?): MutableList<Hit> {
            val speechSearchList = Gson().fromJson(speechTextSearchJson.toString(), HitsJson::class.java).hits
            val translatedTextSearchList = Gson().fromJson(translatedTextSearchJson.toString(), HitsJson::class.java).hits
            return getSearchList(speechSearchList, translatedTextSearchList)
        }

        private fun getSearchList(speechSearchList: List<Hit>, translatedTextSearchList: List<Hit>): MutableList<Hit> {
            val finalList = mutableListOf<Hit>()
            speechSearchList.forEach {
                if (!finalList.contains(it))
                    finalList.add(it)
            }
            translatedTextSearchList.forEach {
                if (!finalList.contains(it))
                    finalList.add(it)
            }
            return finalList
        }
    }
}