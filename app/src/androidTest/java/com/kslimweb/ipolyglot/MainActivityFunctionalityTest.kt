package com.kslimweb.ipolyglot

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityFunctionalityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    val permissionRule = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO)

    /**
     * TODO Test Component Functionality
     */
    @Test
    fun spinnerAndMicrophoneClickable() {
        onView(withId(R.id.spinner_speech_language)).perform(click())
    }
}
