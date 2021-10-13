package com.training.ui.common

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.training.R
import com.training.factory.CommonFragmentFactory
import com.training.fakeactivities.FakeChangePasswordFragmentTestActivity
import com.training.util.constants.ErrorMsg
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ChangePasswordFragmentTest{
    private lateinit var activityScenario: ActivityScenario<FakeChangePasswordFragmentTestActivity>

    @Before
    fun setup() {
        activityScenario = ActivityScenario.launch(FakeChangePasswordFragmentTestActivity::class.java)
        activityScenario.onActivity {
            it.setTestFragment(
                CommonFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(),ChangePasswordFragment::class.java.name)
            )
        }
    }

    @Test fun test_changePasswordFragment_componentsVisisble(){
        Espresso.onView(ViewMatchers.withId(R.id.change_pass_header_txt))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.change_pass_secondary_txt))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.change_pass_txt_error_msg))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordChange))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.confirmButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_changePasswordFragment_ErrorEmptyPassword() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextPasswordChange))
            .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.confirmButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.change_pass_txt_error_msg))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.change_pass_txt_error_msg))
            .check(ViewAssertions.matches(ViewMatchers.withText(ErrorMsg.ERROR_EMPTY_PASSWORD_MSG)))
    }

}
