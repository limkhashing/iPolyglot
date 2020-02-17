package com.kslimweb.ipolyglot.di.module

import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.kslimweb.ipolyglot.BuildConfig
import com.kslimweb.ipolyglot.util.AppConstants.AL_QURAN_INDEX_NAME
import dagger.Module
import dagger.Provides
import io.ktor.client.features.logging.LogLevel
import javax.inject.Singleton

@Module
object AlgoliaModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideIndexSearchClient(): Index = ClientSearch(
        ConfigurationSearch (
            ApplicationID(BuildConfig.AlgoliaAppID),
            APIKey(BuildConfig.AlgoliaSearchKey),
            logLevel = LogLevel.ALL
        )
    ).initIndex(IndexName(AL_QURAN_INDEX_NAME))
}