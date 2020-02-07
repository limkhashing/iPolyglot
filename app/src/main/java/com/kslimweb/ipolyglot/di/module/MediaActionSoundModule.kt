package com.kslimweb.ipolyglot.di.module

import android.media.MediaActionSound
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MediaActionSoundModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideMediaActionSound(): MediaActionSound {
        return MediaActionSound()
    }
}