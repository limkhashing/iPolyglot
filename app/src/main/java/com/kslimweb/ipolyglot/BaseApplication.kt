package com.kslimweb.ipolyglot

import android.app.Application
import com.kslimweb.ipolyglot.di.component.ApplicationComponent
import com.kslimweb.ipolyglot.di.component.DaggerApplicationComponent
import com.kslimweb.ipolyglot.di.module.AlgoliaModule
import com.kslimweb.ipolyglot.di.module.GsonModule
import com.kslimweb.ipolyglot.di.module.TranslateModule

class BaseApplication : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.factory()
            .create(TranslateModule(applicationContext),
                AlgoliaModule,
                GsonModule)
    }

    fun getAppComponent(): ApplicationComponent {
        return component
    }
}