package com.kslimweb.ipolyglot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityFunctionalityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    /**
     * TODO Test Component Functionality
     */
    @Test
    fun spinnerAndMicrophoneClickable() {
        onView(withId(R.id.spinner_speech_language)).perform(click())
    }
}
