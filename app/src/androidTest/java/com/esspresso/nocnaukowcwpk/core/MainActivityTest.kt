package com.esspresso.nocnaukowcwpk.core

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.esspresso.nocnaukowcwpk.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainActivityIsProperlyViewed() {
        onView(withId(R.id.main_activity_root)).check(matches(isDisplayed()))
    }

    @Test
    fun testMainActivityCanOpenMapFragment() {
        onView(withId(R.id.map_square)).perform(click())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test
    fun testMainActivityCanOpenProfileFragmentAndBackToMainActivity() {
        onView(withId(R.id.profile_square)).perform(click())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_root)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_root)).perform(pressBack())
        onView(withId(R.id.main_activity_root)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_root)).check(doesNotExist())
    }

    @Test
    fun testMainActivityCanOpenScannerFragmentAndStartScanning() {
        onView(withId(R.id.scanner_square)).perform(click())
        onView(withId(R.id.list_root)).check(matches(isDisplayed()))
        onView(withId(R.id.scan_image)).perform(click())
    }
}



