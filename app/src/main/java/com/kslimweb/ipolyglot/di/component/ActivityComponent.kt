package com.kslimweb.ipolyglot.di.component

import com.kslimweb.ipolyglot.MainActivity
import com.kslimweb.ipolyglot.di.qualifier.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {

    fun injectActivity(mainActivity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}