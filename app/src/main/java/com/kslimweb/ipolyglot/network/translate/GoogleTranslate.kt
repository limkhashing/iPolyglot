package com.kslimweb.ipolyglot.network.translate

import com.google.cloud.translate.Translate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleTranslate(private val translate: Translate) {
    suspend fun translateText(text: String, translateLanguageCode: String, model: String): String {
        var translatedText = ""
        withContext(Dispatchers.IO) {
            translatedText = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(translateLanguageCode),
                // Use "base" for standard edition, "nmt" for the premium model.
                Translate.TranslateOption.model(model)
            ).translatedText
        }
        return translatedText
    }
}
