package com.kslimweb.ipolyglot.di.module

import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.kslimweb.ipolyglot.BuildConfig
import com.kslimweb.ipolyglot.util.AppConstants.ALGOLIA_INDEX_NAME
import dagger.Module
import dagger.Provides
import io.ktor.client.features.logging.LogLevel
import javax.inject.Singleton

@Module
object AlgoliaModule {

    @Provides
    @JvmStatic
    fun provideAlgoliaClientIndexSearch(): Index = ClientSearch(
        ConfigurationSearch (
            ApplicationID(BuildConfig.AlgoliaAppID),
            APIKey(BuildConfig.AlgoliaSearchKey),
            logLevel = LogLevel.ALL
        )
    ).initIndex(IndexName(ALGOLIA_INDEX_NAME))
}