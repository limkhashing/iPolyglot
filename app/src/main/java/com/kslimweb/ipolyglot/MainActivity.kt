package com.kslimweb.ipolyglot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kslimweb.ipolyglot.databinding.ActivityMainBinding
import com.kslimweb.ipolyglot.network.algolia.Searcher
import com.kslimweb.ipolyglot.network.translate.GoogleTranslate
import com.kslimweb.ipolyglot.speechservices.VoiceRecognizer
import com.kslimweb.ipolyglot.util.AppConstants.REQUEST_AUDIO_PERMISSION
import kotlinx.android.synthetic.main.layout_input_speech.*
import kotlinx.android.synthetic.main.layout_select_translate.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var googleTranslate: GoogleTranslate
    @Inject lateinit var searcher: Searcher

    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION)

        val component = (application as BaseApplication).getAppComponent()
            .getActivityComponentFactory()
            .create()
        component.injectActivity(this)

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        bindData()
        initSpeechRecognizerListener()
        initSpinnerItem()

        spinner_speech_language.setOnItemSelectedListener { view, position, id, item ->
             mainViewModel.speechLanguageCode = mainViewModel.getLanguageCode(position)
//            if (isSpeaking) {
//                setSpeechRecognizerListener()
//                mSpeechRecognizer.startListening(getSpeechRecognizeIntent())
//            }
        }
        spinner_translate_language.setOnItemSelectedListener { view, position, id, item ->
            mainViewModel.translateLanguageCode = mainViewModel.getLanguageCode(position)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (permissions.size != 1 && grantResults.size != 1 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
             showPermissionMessageDialog()
            }
        }
    }

    private fun bindData() {
        mainViewModel = ViewModelProvider(this, MainViewModel(this, mSpeechRecognizer))
            .get(MainViewModel::class.java)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                this.viewModel = mainViewModel
            }
    }

    private fun initSpinnerItem() {
        val adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.list_of_language,
            android.R.layout.simple_spinner_item
        )
        spinner_speech_language.setAdapter(adapter)
        spinner_translate_language.setAdapter(adapter)
    }

    private fun initSpeechRecognizerListener() {
        mSpeechRecognizer.setRecognitionListener(VoiceRecognizer(mSpeechRecognizer,
//            getSpeechRecognizeIntent(),
            this@MainActivity,
            googleTranslate,
            searcher,
            mainViewModel))
    }

    private fun showPermissionMessageDialog() {
        AlertDialog.Builder(applicationContext)
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }.create().show()
    }

    override fun onStop() {
        super.onStop()
        mSpeechRecognizer.stopListening()
        mSpeechRecognizer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSpeechRecognizer.destroy()
    }
}
