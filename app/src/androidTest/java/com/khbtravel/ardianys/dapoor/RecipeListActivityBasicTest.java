package com.khbtravel.ardianys.dapoor;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ardianys on 8/28/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityBasicTest {
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void clickRecipeListThenShowRecipeDetails() {
        onView(withId(R.id.rv_recipes))
                .perform(actionOnItemAtPosition(0, click()));
    }
}
