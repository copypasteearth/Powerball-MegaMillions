/*
 * Author: John Rowan
 * Description:
 * Anyone may use this file or anything contained in this project for their own personal use.
 */
package powerball.apps.jacs.powerball

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
class MainActivityTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    @Test
    fun mainActivityTest() {
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