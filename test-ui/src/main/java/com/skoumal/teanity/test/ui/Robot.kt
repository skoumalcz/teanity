package com.skoumal.teanity.test.ui

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not

abstract class Robot {

    protected val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    fun Int.view() = withId(this).view()
    fun Matcher<View>.view() = onView(this)

    fun ViewInteraction.matchesDrawable(@DrawableRes drawableRes: Int) =
        check(matches(withDrawable(drawableRes)))

    fun ViewInteraction.matchesText(string: String) =
        check(matches(withText(string)))

    fun ViewInteraction.matchesText(@StringRes stringRes: Int) =
        check(matches(withText(stringRes)))

    fun ViewInteraction.containsText(@StringRes stringRes: Int, vararg args: Any) =
        check(matches(withSubstring(stringRes.asString(args))))

    fun ViewInteraction.containsText(string: String) =
        check(matches(withSubstring(string)))

    fun ViewInteraction.matchesHint(@StringRes stringRes: Int) =
        check(matches(withHint(stringRes)))

    fun ViewInteraction.isVisible() =
        check(matches(isDisplayed()))

    fun ViewInteraction.isNotVisible() =
        check(matches(not(isDisplayed())))

    fun ViewInteraction.performClick() =
        perform(click())

    fun ViewInteraction.type(text: String) =
        perform(typeText(text), closeSoftKeyboard())

    fun ViewInteraction.matchesError(@StringRes stringRes: Int) =
        check(matches(hasErrorResource(stringRes)))

    fun ViewInteraction.scrollToView() =
        perform(scrollTo())

    fun ViewInteraction.checked() =
        check(matches(isChecked()))

    fun ViewInteraction.notChecked() =
        check(matches(not(isChecked())))

    fun ViewInteraction.clickable() =
        check(matches(isClickable()))

    fun ViewInteraction.notClickable() =
        check(matches(not(isClickable())))

    fun ViewInteraction.matchesToolbarTitle(@StringRes stringRes: Int, vararg args: Any) =
        check(matches(withTitle(stringRes, args)))

    fun ViewInteraction.matchesToolbarTitle(string: String) =
        check(matches(withTitle(string)))

    fun ViewInteraction.matchesToolbarSubtitle(@StringRes stringRes: Int, vararg args: Any) =
        check(matches(withSubtitle(stringRes, args)))

    fun ViewInteraction.selectAt(position: Int) =
        perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))

    fun ViewInteraction.textExists(@StringRes stringRes: Int, vararg args: Any) =
        textExists(stringRes.asString(args))

    fun ViewInteraction.textExists(string: String) =
        check(matches(hasDescendant(withText(string))))


    protected fun Int.asString(vararg args: Any) = context.getString(this, args)


    /**
     * This identifies current screen by its texts. Focus on static texts which will not change
     * with the screen state and only then check whether the screen's variable texts are valid.
     * */
    abstract fun matchesTexts()


    /**
     * Designed for usage in `companion object`. This ensures one robot touches only one screen
     * or part of your app.
     *
     * Use as such:
     *
     * ```kotlin
     * companion object : Runner<LoginRobot> {
     *      override fun go(body: LoginRobot.() -> Unit) = LoginRobot().apply(body)
     * }
     * ```
     * */
    interface Runner<Robot> {
        fun go(body: Robot.() -> Unit): Robot
    }


}