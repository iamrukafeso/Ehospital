package com.ehospital;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testLoginWithEmptyString()
    {
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.emlEditText)).check(matches(hasErrorText("Please enter email")));

    }

    @Test
    public void testLoginWithIncorrectDetails()
    {
        onView(withId(R.id.emlEditText)).perform(typeText("ruka@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passTextEdit)).perform(typeText("ruke12445"));
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText(R.string.inValidDetails)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));


    }

    @Test
    public void testLoginWithUnverifiedEmail()
    {
        onView(withId(R.id.emlEditText)).perform(typeText("test@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passTextEdit)).perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText(R.string.unVerifiedEmail)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));


    }

    @Test
    public void testLoginAsPatientWithValidDetails()
    {
        onView(withId(R.id.emlEditText)).perform(typeText("iamrukafeso@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passTextEdit)).perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText("Patient Page"));


    }

    @Test
    public void testLoginAsDoctorWithValidDetails()
    {
        onView(withId(R.id.emlEditText)).perform(typeText("hamarow114@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passTextEdit)).perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withText("Doctor Page"));


    }
}
