package com.kslimweb.ipolyglot.util

import com.kslimweb.ipolyglot.BuildConfig

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
