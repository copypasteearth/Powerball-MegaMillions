/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    @Test
    fun mainActivityTest2() {
        val viewPager = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.pager1),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.activity_main),
                                        childAtPosition(
                                                ViewMatchers.withId(R.id.content_frame),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        viewPager.perform(ViewActions.swipeLeft())
        val viewPager2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.pager1),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.activity_main),
                                        childAtPosition(
                                                ViewMatchers.withId(R.id.content_frame),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        viewPager2.perform(ViewActions.swipeRight())
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        val appCompatTextView = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.title), ViewMatchers.withText("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.content),
                                        0),
                                0),
                        ViewMatchers.isDisplayed()))
        appCompatTextView.perform(ViewActions.click())
        val floatingActionButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.drawer_layout),
                                        0),
                                2),
                        ViewMatchers.isDisplayed()))
        floatingActionButton.perform(ViewActions.click())
        val snackbarLayout = Espresso.onView(
                Matchers.allOf(childAtPosition(
                        childAtPosition(
                                ViewMatchers.withId(R.id.drawer_layout),
                                0),
                        3),
                        ViewMatchers.isDisplayed()))
        snackbarLayout.perform(ViewActions.click())
        val appCompatImageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                ViewMatchers.withClassName(Matchers.`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton.perform(ViewActions.click())
        val navigationMenuItemView = Espresso.onView(
                Matchers.allOf(childAtPosition(
                        Matchers.allOf(ViewMatchers.withId(R.id.design_navigation_view),
                                childAtPosition(
                                        ViewMatchers.withId(R.id.nav_view),
                                        0)),
                        1),
                        ViewMatchers.isDisplayed()))
        navigationMenuItemView.perform(ViewActions.click())
        val appCompatImageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                ViewMatchers.withClassName(Matchers.`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton2.perform(ViewActions.click())
        val navigationMenuItemView2 = Espresso.onView(
                Matchers.allOf(childAtPosition(
                        Matchers.allOf(ViewMatchers.withId(R.id.design_navigation_view),
                                childAtPosition(
                                        ViewMatchers.withId(R.id.nav_view),
                                        0)),
                        3),
                        ViewMatchers.isDisplayed()))
        navigationMenuItemView2.perform(ViewActions.click())
        val appCompatImageButton3 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                ViewMatchers.withClassName(Matchers.`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton3.perform(ViewActions.click())
        val navigationMenuItemView3 = Espresso.onView(
                Matchers.allOf(childAtPosition(
                        Matchers.allOf(ViewMatchers.withId(R.id.design_navigation_view),
                                childAtPosition(
                                        ViewMatchers.withId(R.id.nav_view),
                                        0)),
                        2),
                        ViewMatchers.isDisplayed()))
        navigationMenuItemView3.perform(ViewActions.click())
        val appCompatImageButton1 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                ViewMatchers.withClassName(Matchers.`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton1.perform(ViewActions.click())
        Espresso.pressBack()
    }

    companion object {
        private fun childAtPosition(
                parentMatcher: Matcher<View>, position: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return (parent is ViewGroup && parentMatcher.matches(parent)
                            && view == parent.getChildAt(position))
                }
            }
        }
    }
}