package com.kslimweb.ipolyglot.di.module

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.kslimweb.ipolyglot.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TranslateModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideTranslateClient(): Translate = context.resources.openRawResource(R.raw.translation_services_account).use { `is` ->
        val myCredentials = GoogleCredentials.fromStream(`is`)
        val translateOptions =
            TranslateOptions.newBuilder().setCredentials(myCredentials).build()
        return translateOptions.service
    }
}