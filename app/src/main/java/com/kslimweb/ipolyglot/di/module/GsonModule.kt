package com.kslimweb.ipolyglot.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object GsonModule {
    @Provides
    @JvmStatic
    @Singleton
    fun provideGson(): Gson = Gson()
}