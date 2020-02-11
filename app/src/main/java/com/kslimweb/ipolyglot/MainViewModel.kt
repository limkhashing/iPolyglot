package com.kslimweb.ipolyglot

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaActionSound
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kslimweb.ipolyglot.util.AppConstants.listeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.notListeningStatus
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStart
import com.kslimweb.ipolyglot.util.AppConstants.speechToTextButtonTextStop
import java.util.*


class MainViewModel(application: Application, private val mSpeechRecognizer: SpeechRecognizer, private val mediaActionSound: MediaActionSound) :
    ViewModelProvider.Factory, AndroidViewModel(application) {

    val context: Context = application.applicationContext

    val isSpeaking = ObservableBoolean(false)
//    val textButtonSpeechToText = ObservableField<String>(speechToTextButtonTextStart)
//    val textListeningStatus = ObservableField<String>(notListeningStatus)
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
                mediaActionSound
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    fun onVoiceFinished(error: Boolean = false) {
//        textButtonSpeechToText.set(speechToTextButtonTextStart)
//        textListeningStatus.set(notListeningStatus)
        if (error)
            mediaActionSound.play(MediaActionSound.STOP_VIDEO_RECORDING)
        isSpeaking.set(!isSpeaking.get())
    }

    fun onInputSpeechClicked() {
        isSpeaking.set(!isSpeaking.get()) // flip the boolean isSpeaking

        if (!isSpeaking.get()) {
            mSpeechRecognizer.stopListening()
//            textButtonSpeechToText.set(speechToTextButtonTextStart)
//            textListeningStatus.set(notListeningStatus)
        } else {
            mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
//            textButtonSpeechToText.set(speechToTextButtonTextStop)
//            textListeningStatus.set(listeningStatus)
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