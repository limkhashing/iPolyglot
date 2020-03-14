package com.kslimweb.ipolyglot.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
object DataModule {

    const val ALGOLIA_SEARCH_PREF = "ALGOLIA_SEARCH_PREF"

    @Provides
    @Singleton
    @Named(ALGOLIA_SEARCH_PREF)
    fun provideAlgoliaSearchSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(ALGOLIA_SEARCH_PREF, Context.MODE_PRIVATE)
    }
}