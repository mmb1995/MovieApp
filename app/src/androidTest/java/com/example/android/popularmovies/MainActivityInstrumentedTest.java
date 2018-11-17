package com.example.android.popularmovies;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {


    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    /**
     * Makes sure the spinner responds to user selection
     */
    @Test
    public void clickSpinnerItemChangesSelectedItem()  {
        onView(withId(R.id.action_dropdown)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.action_dropdown)).check(matches(withSpinnerText("Top Rated")));
    }

}
