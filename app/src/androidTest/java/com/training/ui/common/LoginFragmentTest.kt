package com.training.ui.common

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.training.R
import com.training.factory.CommonFragmentFactory
import com.training.fakeactivities.FakeLoginFragmentTestActivity
import com.training.util.constants.ErrorMsg
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class LoginFragmentTest {

    private lateinit var activityScenarioLoginFragment: ActivityScenario<FakeLoginFragmentTestActivity>

    @Before
    fun setup() {
        activityScenarioLoginFragment = ActivityScenario.launch(FakeLoginFragmentTestActivity::class.java)
        activityScenarioLoginFragment.onActivity {
            it.setTestFragment(
                CommonFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(),LoginFragment::class.java.name)
            )
        }
    }

    @Test
    fun test_LoginFragment_ComponentsVisible() {
        onView(withId(R.id.login_header_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.login_txt_error_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_nav_log)).check(matches(isDisplayed()))
        onView(withId(R.id.login_imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_LoginFragment_ErrorEmptyEmail() {
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.login_txt_error_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.login_txt_error_msg)).check(matches(withText(ErrorMsg.ERROR_EMPTY_EMAIL_MSG)))
    }

    @Test
    fun test_LoginFragment_ErrorEmptyPassword() {
        onView(withId(R.id.editTextEmail)).perform(typeText("email@email.com"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.login_txt_error_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.login_txt_error_msg)).check(matches(withText(ErrorMsg.ERROR_EMPTY_PASSWORD_MSG)))
    }

    @Test
    fun test_LoginFragment_ErrorInvalidEmail() {
        onView(withId(R.id.editTextEmail)).perform(typeText("email"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.login_txt_error_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.login_txt_error_msg)).check(matches(withText(ErrorMsg.ERROR_EMAIL_NOT_VALID_MSG)))
    }

    @Test
    fun test_LoginFragment_ValidDataEntery() {
        onView(withId(R.id.editTextEmail)).perform(typeText("email@email.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.login_txt_error_msg)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.progress_bar_login)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}