package com.kslimweb.ipolyglot.di.module

import android.content.Context
import android.content.SharedPreferences
import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.gson.Gson
import com.kslimweb.ipolyglot.BuildConfig
import com.kslimweb.ipolyglot.R
import com.kslimweb.ipolyglot.di.module.DataModule.ALGOLIA_SEARCH_PREF
import com.kslimweb.ipolyglot.network.algolia.AlgoliaSearcher
import com.kslimweb.ipolyglot.util.AppConstants
import com.kslimweb.ipolyglot.util.extension.AlQuranSearchHelper
import com.kslimweb.ipolyglot.util.extension.VersionUtils
import dagger.Module
import dagger.Provides
import io.ktor.client.features.logging.LogLevel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApplicationModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideIndexSearchClient(): Index = ClientSearch(
        ConfigurationSearch (
            ApplicationID(BuildConfig.AlgoliaAppID),
            APIKey(BuildConfig.AlgoliaSearchKey),
            logLevel = LogLevel.ALL
        )
    ).initIndex(IndexName(VersionUtils.algoliaIndexName()))


    @Provides
    @JvmStatic
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @JvmStatic
    @Singleton
    fun provideKotlinxJson(): Json = Json(JsonConfiguration.Stable)

    @Provides
    @Singleton
    fun provideBgScope(bgDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(bgDispatcher)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideBgDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideUiDispatcher(): MainCoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Singleton
    fun provideTranslateClient(context: Context): Translate = context.resources.openRawResource(R.raw.translation_services_account).use { `is` ->
        val myCredentials = GoogleCredentials.fromStream(`is`)
        val translateOptions =
            TranslateOptions.newBuilder().setCredentials(myCredentials).build()
        return translateOptions.service
    }

    @Singleton
    @Provides
    fun provideAlQuranSearchResultHelper(@Named(ALGOLIA_SEARCH_PREF) sharedPreferences: SharedPreferences,
                                         json: Json, searcher: AlgoliaSearcher): AlQuranSearchHelper {
        return AlQuranSearchHelper(sharedPreferences, json, searcher)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideAlgoliaSearcher(index: Index, bgDispatcher: CoroutineDispatcher): AlgoliaSearcher =
        AlgoliaSearcher(index, bgDispatcher)
}
