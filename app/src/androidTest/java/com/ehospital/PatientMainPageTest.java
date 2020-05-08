package com.ehospital;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class PatientMainPageTest {
    @Rule
    public ActivityScenarioRule<PatientMainActivity> mActivityRule = new ActivityScenarioRule<PatientMainActivity>(PatientMainActivity.class);



    @Test
    public void testSwipingPatientFragments()
    {
        onView(withText("ALL DOCTORS")).perform(click());
        onView(withText("HEALTH NEWS")).perform(click());


    }

    @Test
    public void testPatientTryToSeeDoctorProfile()
    {
        onView(withText("ALL DOCTORS")).perform(click());
        onView(withId(R.id.doctorListId)).check(matches(isDisplayed()));
      //  onView(withId(R.id.doctorListId)).perform(

        onView(withId(R.id.doctorListId))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withText("Profile")).perform(click());
        onView(withText("Doctor Profile")).perform(click());



    }
}
