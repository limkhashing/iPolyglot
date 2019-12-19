package com.kslimweb.ipolyglot.util

import android.content.Context
import com.algolia.search.saas.Client
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.gson.Gson
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.AlgorliaCredentials

class CredentialsHelper(private val context: Context) {

    fun initGoogleTranslateClient(): Translate {
        context.resources.openRawResource(R.raw.credential_dev).use { `is` ->
            val myCredentials = GoogleCredentials.fromStream(`is`)
            val translateOptions =
                TranslateOptions.newBuilder().setCredentials(myCredentials).build()
            return translateOptions.service
        }
    }

    fun initAlgoliaClient(): Client {
        val algoliaJson = context.resources.openRawResource(R.raw.algolia_dev).bufferedReader().use { it.readText() }
        val algoliaCredentials = Gson().fromJson(algoliaJson, AlgorliaCredentials::class.java)
        return Client(algoliaCredentials.appId, algoliaCredentials.apiSearchKey)
    }
}