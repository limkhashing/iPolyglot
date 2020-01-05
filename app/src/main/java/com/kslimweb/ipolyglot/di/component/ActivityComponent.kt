package com.kslimweb.ipolyglot.di.component

import com.kslimweb.ipolyglot.MainActivity
import com.kslimweb.ipolyglot.di.qualifier.PerActivity
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizer
import dagger.Subcomponent

@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}