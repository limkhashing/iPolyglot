package com.kslimweb.ipolyglot

import android.app.Application
import com.kslimweb.ipolyglot.di.component.AppComponent
import com.kslimweb.ipolyglot.di.component.DaggerAppComponent
import com.kslimweb.ipolyglot.di.module.AlgoliaModule
import com.kslimweb.ipolyglot.di.module.MediaActionSoundModule
import com.kslimweb.ipolyglot.di.module.TranslateModule

class BaseApplication : Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory()
            .create(TranslateModule(applicationContext), AlgoliaModule, MediaActionSoundModule)
    }

    fun getAppComponent(): AppComponent {
        return component
    }
}