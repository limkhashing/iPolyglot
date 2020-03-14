package com.kslimweb.ipolyglot.di.component

import android.content.Context
import com.kslimweb.ipolyglot.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun getActivityComponentFactory() : ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        // if you have other dependencies, put as arguments in methods
        fun create(
            @BindsInstance applicationContext: Context,
            applicationModule: ApplicationModule,
            dataModule: DataModule
        ): ApplicationComponent
    }
}