package com.kslimweb.ipolygot.util

import android.content.Context
import com.algolia.search.saas.AlgoliaException
import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.gson.Gson
import com.kslimweb.ipolygot.R
import com.kslimweb.ipolygot.SpeechTranslateAdapter
import com.kslimweb.ipolygot.algolia_data.AlgorliaCredentials
import com.kslimweb.ipolygot.algolia_data.HitsJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
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
                8-> languageCode = "ja"
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

        suspend fun translateText(translate: Translate, text: String, translateLanguageCode: String): String {
            var translatedText = ""
            withContext(IO) {
                translatedText = translate.translate(text,
                    Translate.TranslateOption.targetLanguage(translateLanguageCode),
                    // Use "base" for standard edition, "nmt" for the premium model.
                    Translate.TranslateOption.model("base")).translatedText
            }
            return translatedText
        }

        fun algoliaSearchCallback(speechToText: String, translatedText: String, resultAdapter: SpeechTranslateAdapter, index: Index) {
            index.searchAsync(Query(translatedText)) { jsonObject: JSONObject?, algoliaException: AlgoliaException? ->
                val hitsJson = Gson().fromJson(jsonObject.toString(), HitsJson::class.java)
                resultAdapter.addResult(speechToText, translatedText, hitsJson.hits)
            }
        }
    }
}