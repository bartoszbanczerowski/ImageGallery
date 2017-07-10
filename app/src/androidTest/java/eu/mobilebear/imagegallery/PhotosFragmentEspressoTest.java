package eu.mobilebear.imagegallery;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PhotosFragmentEspressoTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void testSearchingItems() {
    ViewInteraction textInputEditText = onView(
        allOf(withId(R.id.tagEditTextView), isDisplayed()));
    textInputEditText.perform(replaceText("test"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.searchButton), withText("SEARCH"),
            withParent(allOf(withId(R.id.mainLayout),
                withParent(withId(R.id.container)))),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction editText = onView(
        allOf(withId(R.id.tagEditTextView), withText("test"), childAtPosition(childAtPosition(
            IsInstanceOf.<View>instanceOf(TextInputLayout.class), 0), 0), isDisplayed()));
    editText.check(matches(withText("test")));

    ViewInteraction editText2 = onView(
        allOf(withId(R.id.tagEditTextView), withText("test"), childAtPosition(childAtPosition(
            IsInstanceOf.<View>instanceOf(TextInputLayout.class), 0), 0), isDisplayed()));
    editText2.check(matches(withText("test")));

    ViewInteraction switch_ = onView(
        allOf(withId(R.id.sortSwitch), childAtPosition(allOf(withId(R.id.mainLayout),
            childAtPosition(withId(R.id.container), 0)), 2), isDisplayed()));
    switch_.check(matches(isDisplayed()));

    ViewInteraction recyclerView = onView(
        allOf(withId(R.id.photosRecyclerView),
            childAtPosition(allOf(withId(R.id.mainLayout),
                childAtPosition(withId(R.id.container), 0)), 3), isDisplayed()));
    recyclerView.check(matches(isDisplayed()));
  }

  @Test
  public void testSortSwitch() {
    ViewInteraction textInputEditText = onView(
        allOf(withId(R.id.tagEditTextView), isDisplayed()));
    textInputEditText.perform(replaceText("test"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.searchButton), withText("SEARCH"),
            withParent(allOf(withId(R.id.mainLayout),
                withParent(withId(R.id.container)))), isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction switchCompat = onView(
        allOf(withId(R.id.sortSwitch), withText("Sort"), withParent(allOf(withId(R.id.mainLayout),
            withParent(withId(R.id.container)))), isDisplayed()));

    onView(withId(R.id.sortSwitch)).check(matches(not(isChecked())));

    switchCompat.perform(click());

    ViewInteraction switch_ = onView(
        allOf(withId(R.id.sortSwitch), childAtPosition(allOf(withId(R.id.mainLayout),
            childAtPosition(withId(R.id.container), 0)), 2), isDisplayed()));
    switch_.check(matches(isDisplayed()));

    ViewInteraction switch_2 = onView(
        allOf(withId(R.id.sortSwitch), childAtPosition(allOf(withId(R.id.mainLayout),
            childAtPosition(withId(R.id.container), 0)), 2), isDisplayed()));
    switch_2.check(matches(isDisplayed()));

    onView(withId(R.id.sortSwitch)).check(matches(isChecked()));
  }

  @Test
  public void testRecyclerViewItem() {
    ViewInteraction textInputEditText = onView(
        allOf(withId(R.id.tagEditTextView), isDisplayed()));
    textInputEditText.perform(replaceText("test"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.searchButton), withText("SEARCH"),
            withParent(allOf(withId(R.id.mainLayout),
                withParent(withId(R.id.container)))), isDisplayed()));
    appCompatButton.perform(click());

    SystemClock.sleep(1500);

    onView(withId(R.id.photosRecyclerView))
        .perform(scrollToPosition(0))
        .perform(actionOnItemAtPosition(0, click()));

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(allOf(withId(R.id.photoCardView),
            childAtPosition(withId(R.id.photosRecyclerView), 0)), 0), isDisplayed()));
    linearLayout.check(matches(isDisplayed()));

    ViewInteraction imageView = onView(
        allOf(withId(R.id.photoImageView), childAtPosition(
            childAtPosition(withId(R.id.photoCardView), 0), 0), isDisplayed()));
    imageView.check(matches(isDisplayed()));

    ViewInteraction titleTextView = onView(
        allOf(withId(R.id.titleTextVIew),
            childAtPosition(childAtPosition(withId(R.id.photoCardView), 0), 1), isDisplayed()));
    titleTextView.check(matches(isDisplayed()));

    ViewInteraction dateTakenPrefixTextView = onView(
        allOf(withText("Date taken:"), childAtPosition(childAtPosition(
            IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 2), 0),
            isDisplayed()));
    dateTakenPrefixTextView.check(matches(withText("Date taken:")));

    ViewInteraction dateTakenTextView = onView(
        allOf(withId(R.id.dataTakenTextVIew), childAtPosition(childAtPosition(
            IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
            2), 1), isDisplayed()));
    dateTakenTextView.check(matches(isDisplayed()));

    ViewInteraction datePublishPrefixTextView = onView(
        allOf(withText("Date published:"),
            childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 3), 0),
            isDisplayed()));
    datePublishPrefixTextView.check(matches(withText("Date published:")));

    ViewInteraction publishTestView = onView(
        allOf(withId(R.id.publishedTextVIew), childAtPosition(childAtPosition(
            IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 3), 1),
            isDisplayed()));
    publishTestView.check(matches(isDisplayed()));

    ViewInteraction authorPrefixTextView = onView(
        allOf(withText("Author:"),
            childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                4), 0), isDisplayed()));
    authorPrefixTextView.check(matches(withText("Author:")));

    ViewInteraction authorTextView = onView(
        allOf(withId(R.id.authorTextVIew),
            childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                4), 1), isDisplayed()));
    authorTextView.check(matches(isDisplayed()));
  }


  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
