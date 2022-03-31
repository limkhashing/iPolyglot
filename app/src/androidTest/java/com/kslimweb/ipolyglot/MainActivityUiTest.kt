package com.kslimweb.ipolyglot

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityUiTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    val permissionRule = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO)

    /**
     * Test UI
     */
    @Test
    fun isActivityInView() {
        onView(withId(R.id.main_view)).check(matches(isDisplayed()))
    }

    @Test
    fun spinnerTextMicrophoneIsVisible() {
        onView(withId(R.id.txt_input_speech)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_speech_language)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_translation_language)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_translate_language)).check(matches(isDisplayed()))
        onView(withId(R.id.image_google_microphone)).check(matches(isDisplayed()))
    }

    @Test
    fun cardViewComponentVisibility() {
        onView(withId(R.id.card_linear_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_speech)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_translation)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_label_search)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_search)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkTextDisplayed() {
        onView(withId(R.id.txt_input_speech)).check(matches(withText(R.string.spinner_text_input_speech)))
        onView(withId(R.id.txt_translation_language)).check(matches(withText(R.string.spinner_text_translation)))
        onView(withId(R.id.txt_speech)).check(matches(withText(R.string.speech_text)))
        onView(withId(R.id.txt_translation)).check(matches(withText(R.string.translation_text)))
        onView(withId(R.id.txt_label_search)).check(matches(withText(R.string.label_search)))
    }
}
