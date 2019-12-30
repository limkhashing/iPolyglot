package com.kslimweb.ipolyglot.util

import android.content.Context
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.gson.Gson
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.model.AlgorliaCredentials
import io.ktor.client.features.logging.LogLevel

class CredentialsHelper(private val context: Context) {

    fun initGoogleTranslateClient(): Translate {
        // TODO change your own credential
        context.resources.openRawResource(R.raw.credential_dev).use { `is` ->
            val myCredentials = GoogleCredentials.fromStream(`is`)
            val translateOptions =
                TranslateOptions.newBuilder().setCredentials(myCredentials).build()
            return translateOptions.service
        }
    }

    fun initAlgoliaClient(): ClientSearch {
        val algoliaJson = context.resources.openRawResource(R.raw.algolia_dev).bufferedReader().use { it.readText() }
        val algoliaCredentials = Gson().fromJson(algoliaJson, AlgorliaCredentials::class.java)
        return ClientSearch(
            ConfigurationSearch (
                ApplicationID(algoliaCredentials.appId),
                APIKey(algoliaCredentials.apiSearchKey),
                logLevel = LogLevel.ALL
            )
        )
    }
}