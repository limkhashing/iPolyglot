package com.kslimweb.ipolyglot

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaActionSound
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*


class MainViewModel(
    application: Application,
    private val mSpeechRecognizer: SpeechRecognizer,
    private val mainDispatcher: MainCoroutineDispatcher
) : ViewModelProvider.Factory, AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    var speechText = ObservableField(context.getString(R.string.speech_text))
    var translationText = ObservableField(context.getString(R.string.translation_text))
    var appearInLabelText = ObservableField(context.getString(R.string.label_search))
    var searchRecyclerViewVisibility = ObservableBoolean(false)

    val isSpeaking = ObservableBoolean(false)
    var speechLanguageCode = getLanguageCode()
    var translateLanguageCode = getLanguageCode()

    // For ViewModel to accept constructor from ViewModelProvider
    // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            // Or better use here if it doesn't provides error @SuppressWarnings("unchecked")
            return MainViewModel(
                context as Application,
                mSpeechRecognizer,
                mainDispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    fun onVoiceFinished(error: Boolean = false) {
        mSpeechRecognizer.stopListening()
        isSpeaking.set(false)
    }

    fun onInputSpeechClicked() {
        isSpeaking.set(!isSpeaking.get()) // flip the boolean isSpeaking
        if (isSpeaking.get()) {
            mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
        } else {
            mSpeechRecognizer.stopListening()
        }
    }

    suspend fun setSpeechAndTranslationText(speechText: String, translatedText: String) {
        withContext(mainDispatcher) {
            this@MainViewModel.speechText.set(speechText)
            this@MainViewModel.translationText.set(translatedText)
        }
    }

    private fun getSpeechRecognizeIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        //Customize language by passing language code
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLanguageCode)
        if (speechLanguageCode == "ar") {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-IL")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-JO")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-AE")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-BH")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-DZ")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-IQ")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-KW")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-MA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-TN")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-OM")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-PS")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-QA")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-LB")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG")
        }

        //To receive partial results on the callback
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        return intent
    }

    fun getLanguageCode(position: Int = 0): String {
        var languageCode = ""
        when (position) {
            0 -> languageCode = "ar"
            1 -> languageCode = "ms"
            2 -> languageCode = "zh"
            3 -> languageCode = "en"
            4 -> languageCode = "fr"
            5 -> languageCode = "de"
            6 -> languageCode = "hi"
            7 -> languageCode = "it"
            8 -> languageCode = "ja"
            9 -> languageCode = "ko"
            10 -> languageCode = "pa"
            11 -> languageCode = "ta"
            12 -> languageCode = "tl"
            13 -> languageCode = "th"
            14 -> languageCode = "vi"
        }
        return languageCode
    }
}