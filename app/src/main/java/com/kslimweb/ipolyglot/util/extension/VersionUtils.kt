package com.kslimweb.ipolyglot.util.extension

import com.kslimweb.ipolyglot.BuildConfig
import com.kslimweb.ipolyglot.util.AppConstants

object VersionUtils {

    val isReleaseMode: Boolean
        get() = !BuildConfig.DEBUG


    fun algoliaIndexName(): String {
        return if (!isReleaseMode)
            AppConstants.DEV_AL_QURAN_INDEX_NAME
        else
            AppConstants.AL_QURAN_INDEX_NAME
    }
}
