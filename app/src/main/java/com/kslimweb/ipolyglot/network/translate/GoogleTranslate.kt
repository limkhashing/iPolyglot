package com.kslimweb.ipolyglot.network.translate

import com.google.cloud.translate.Translate
import com.kslimweb.ipolyglot.util.AppConstants.TRANSLATE_MODEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleTranslate @Inject constructor(private val googleTranslateClient: Translate) {

    suspend fun translateText(text: String, translateLanguageCode: String): String {
        var translatedText = ""
        withContext(Dispatchers.IO) {
            translatedText = googleTranslateClient.translate(
                text,
                Translate.TranslateOption.targetLanguage(translateLanguageCode),
                // Use "base" for standard edition, "nmt" for the premium model.
                Translate.TranslateOption.model(TRANSLATE_MODEL)
            ).translatedText
        }
        return translatedText
    }
}
