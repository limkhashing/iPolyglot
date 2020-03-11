package com.kslimweb.ipolyglot

import android.app.Application
import com.facebook.stetho.Stetho
import com.kslimweb.ipolyglot.di.component.ApplicationComponent
import com.kslimweb.ipolyglot.di.component.DaggerApplicationComponent
import com.kslimweb.ipolyglot.di.module.*

class BaseApplication : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        component = DaggerApplicationComponent.factory()
            .create(applicationContext,
                ApplicationModule,
                DataModule)
    }

    fun getAppComponent(): ApplicationComponent {
        return component
    }
}
