package com.ehospital;


import android.view.View;

import androidx.annotation.ContentView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Test;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class RegistratrionTest  {


    @Rule
    public ActivityScenarioRule<Registratrion> mActivityRule = new ActivityScenarioRule<Registratrion>(Registratrion.class);
//    @Rule
//    public ActivityTestRule<Registratrion> mActivityRule = new ActivityTestRule<>(
//            Registratrion.class);
//

//    @Before
//    void setUp(){
//        mActivityRule.getActivity();
//    }

    @Test
    public void onCreate() {
    }

    @Test
    public void regProcess()
    {

    }

    @Test

    public void testFirstNameWithEmptyString() throws Exception
    {

       // onView(withId(R.id.firstNameTextEdit)).perform(typeText("Abdi"));
       // closeSoftKeyboard();
     //  firstName = onView(withHint("First Name"));
       //firstName = onView(withText("Abdulkadir"));

       // onView(withId(R.id.firstNameTextEdit)).
       onView(withId(R.id.signupbtn)).perform(click());
       onView(withId(R.id.firstNameTextEdit)).check(matches(hasErrorText("Please enter first name")));

        //onView(withId(R.id.signupbtn)).perform((ViewAction) isClickable());
    }

    @Test

    public void testRegistrationWithValidDetails() throws Exception
    {

         onView(withId(R.id.firstNameTextEdit)).perform(typeText("Abdulkadir"));
         onView(withId(R.id.surnameTextEdit)).perform(typeText("Hamarow"));
         onView(withId(R.id.emailEdit)).perform(typeText("h124@gmail.com"));
        closeSoftKeyboard();
         onView(withId(R.id.passEdit)).perform(typeText("password"));
        closeSoftKeyboard();

        onView(withId(R.id.dateOfBirth)).perform(typeText("02/12/1998"));
        onView(withId(R.id.accTypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Patient"))).perform(click());
         closeSoftKeyboard();
        //  firstName = onView(withHint("First Name"));
        //firstName = onView(withText("Abdulkadir"));

        // onView(withId(R.id.firstNameTextEdit)).
        onView(withId(R.id.signupbtn)).perform(click());
       // onView(withId(R.id.firstNameTextEdit)).check(matches(hasN("Please enter first name")));

        //onView(withId(R.id.signupbtn)).perform((ViewAction) isClickable());
    }

    @Test

    public void testRegistrationWithInvalidEmail() throws Exception
    {

         onView(withId(R.id.firstNameTextEdit)).perform(typeText("Abdulkadir"));
         onView(withId(R.id.surnameTextEdit)).perform(typeText("Hamarow"));
         onView(withId(R.id.emailEdit)).perform(typeText("hamarow114@gmail.com"));
        closeSoftKeyboard();
         onView(withId(R.id.passEdit)).perform(typeText("password"));
        closeSoftKeyboard();

        onView(withId(R.id.dateOfBirth)).perform(typeText("02/12/1998"));
        closeSoftKeyboard();
        onView(withId(R.id.accTypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Doctor"))).perform(click());
        //onView(withId(R.id.accTypeSpinner)).check(matches(withSpinnerText(containsString("Doctor"))));
      //  onView(allOf(withId(R.id.accTypeSpinner), withText("Doctor"))).perform(click());
        // onView(withId(R.id.accTypeSpinner)).perform(typeText("Doctor"));
         closeSoftKeyboard();
        //  firstName = onView(withHint("First Name"));
        //firstName = onView(withText("Abdulkadir"));

        // onView(withId(R.id.firstNameTextEdit)).
        onView(withId(R.id.signupbtn)).perform(click());


        onView(withText(R.string.toast)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
       // onView(withId(R.id.firstNameTextEdit)).check(matches(hasN("Please enter first name")));

        //onView(withId(R.id.signupbtn)).perform((ViewAction) isClickable());
    }


}