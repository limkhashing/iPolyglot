package com.kslimweb.ipolyglot.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Singleton

@Module
object CoroutineModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideBgScope(bgDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(bgDispatcher)
    }

//    @Provides
//    @JvmStatic
//    @Singleton
//    @Named("uiScope")
//    fun provideUiScope(mainDispatcher: MainCoroutineDispatcher): CoroutineScope {
//        return CoroutineScope(mainDispatcher)
//    }

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
}
