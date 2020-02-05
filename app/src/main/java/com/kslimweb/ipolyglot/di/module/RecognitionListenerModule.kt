package com.kslimweb.ipolyglot.di.module

import android.speech.RecognitionListener
import com.kslimweb.ipolyglot.di.qualifier.PerActivity
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizer
import dagger.Binds
import dagger.Module

@Module
abstract class RecognitionListenerModule {
    @Binds
    @PerActivity
    abstract fun bindRecognitionListener(voiceRecognizer: VoiceRecognizer): RecognitionListener
}