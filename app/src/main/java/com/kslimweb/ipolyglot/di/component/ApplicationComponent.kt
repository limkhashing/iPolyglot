package com.kslimweb.ipolyglot.di.component

import com.kslimweb.ipolyglot.di.module.AlgoliaModule
import com.kslimweb.ipolyglot.di.module.GsonModule
import com.kslimweb.ipolyglot.di.module.TranslateModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TranslateModule::class, AlgoliaModule::class, GsonModule::class])
interface ApplicationComponent {

    fun getActivityComponentFactory() : ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        // if you have other dependencies, put as arguments in methods
        fun create(translateModule: TranslateModule,
                   algoliaModule: AlgoliaModule,
                   gsonModule: GsonModule): ApplicationComponent
    }
}