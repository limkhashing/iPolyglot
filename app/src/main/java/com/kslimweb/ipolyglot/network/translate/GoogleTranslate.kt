package com.kslimweb.ipolyglot.network.translate

import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.util.AppConstants.TRANSLATE_MODEL_NMT
import kotlinx.coroutines.*
import javax.inject.Inject

class GoogleTranslate @Inject constructor(private val googleTranslateClient: Translate,
            private val bgDispatcher: CoroutineDispatcher) {

    suspend fun translateText(text: String, translateLanguageCode: String): String {
        return withContext(bgDispatcher) {
            googleTranslateClient.translate(
                text,
                Translate.TranslateOption.targetLanguage(translateLanguageCode),
                // Use "base" for standard edition, "nmt" for the premium model.
                Translate.TranslateOption.model(TRANSLATE_MODEL_NMT)
            ).translatedText
        }
    }
}
