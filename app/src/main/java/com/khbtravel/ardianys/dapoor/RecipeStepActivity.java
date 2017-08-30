/*
* Copyright (C) 2017 khbtravel.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* twitter.com/ardianys
* August 2017
*/

package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.khbtravel.ardianys.dapoor.fragment.RecipeStepFragment;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;

import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_BOOL_TABLET_MODE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_STEP;

public class RecipeStepActivity extends AppCompatActivity {

    Recipe recipe;
    Step step;
    Bundle mBundle;
    private Boolean mTabletMode = false;
    public static final String TAG = "RecipeStepActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Log.e(TAG, 111111 + " onCreate");

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(INTENT_PARCEL_RECIPE);
            step = savedInstanceState.getParcelable(INTENT_PARCEL_STEP);
            mTabletMode = savedInstanceState.getBoolean(INTENT_BOOL_TABLET_MODE);
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_PARCEL_RECIPE)) {
                recipe = intent.getParcelableExtra(INTENT_PARCEL_RECIPE);
            }
            if (intent.hasExtra(INTENT_PARCEL_STEP)) {
                step = intent.getParcelableExtra(INTENT_PARCEL_STEP);
            }
            if (intent.hasExtra(INTENT_BOOL_TABLET_MODE)) {
                mTabletMode = intent.getBooleanExtra(INTENT_BOOL_TABLET_MODE, false);
            }
        }

        if(savedInstanceState == null) {
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            replaceFragment(step, recipeStepFragment);
        } else {
            RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().
                    findFragmentByTag(RecipeStepFragment.TAG);
            replaceFragment(step, recipeStepFragment);
        }
    }

    private void replaceFragment(Step step, RecipeStepFragment recipeStepFragment) {
        if (recipeStepFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(INTENT_PARCEL_RECIPE, recipe);
            bundle.putParcelable(INTENT_PARCEL_STEP, step);
            recipeStepFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_step_container, recipeStepFragment, RecipeStepFragment.TAG).
                addToBackStack(null).
                commit();
    }

    public void clickPrevious(View view){
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        replaceFragment((Step) view.getTag(), recipeStepFragment);
    }

    public void clickNext(View view){
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        replaceFragment((Step) view.getTag(), recipeStepFragment);
    }

}
