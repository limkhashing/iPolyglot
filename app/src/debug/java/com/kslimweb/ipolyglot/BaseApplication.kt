package com.kslimweb.ipolyglot

import com.facebook.stetho.Stetho

class BaseApplicationDebug : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
