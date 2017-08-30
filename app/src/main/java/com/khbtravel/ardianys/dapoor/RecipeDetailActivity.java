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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

import com.khbtravel.ardianys.dapoor.fragment.MasterListInterface;
import com.khbtravel.ardianys.dapoor.fragment.RecipeDetailFragment;
import com.khbtravel.ardianys.dapoor.fragment.RecipeStepFragment;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.khbtravel.ardianys.dapoor.widget.RecipeWidgetService;

import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_BOOL_TABLET_MODE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_STEP;

public class RecipeDetailActivity extends AppCompatActivity
        implements MasterListInterface {

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewStep;

    private Boolean mTabletMode = false;
    private Recipe mRecipe;
    private Step mStep;

    public static final String RECYCLER_VIEW_STEP = "RECYCLER_VIEW_STEP";
    public static final String SHARED_PREFS_INGREDIENTS = "SHARED_PREFS_INGREDIENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(INTENT_PARCEL_RECIPE);
        }

        // Get Intent and check for mRecipe ID that added in intent's extra
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_PARCEL_RECIPE)) {
                mRecipe = intent.getParcelableExtra(INTENT_PARCEL_RECIPE);
            }
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFS_INGREDIENTS, mRecipe.buildIngredients());
        editor.apply();

        RecipeWidgetService.startActionUpdateWidgets(getApplicationContext(), mRecipe);


        if(findViewById(R.id.recipe_step_container)!= null){
            mTabletMode = true;
        }

        if (isTablet()){
            if(savedInstanceState == null) {
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                replaceStepFragment(0, recipeStepFragment);
            } else {
                mStep = savedInstanceState.getParcelable(INTENT_PARCEL_STEP);
                RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().
                        findFragmentByTag(RecipeStepFragment.TAG);
                replaceStepFragment(0, recipeStepFragment);
            }
        }

        if(savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(this);
            replaceDetailFragment(mRecipe, recipeDetailFragment);
        } else {
            RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().
                    findFragmentByTag(RecipeDetailFragment.TAG);
            recipeDetailFragment.setListener(this);
            replaceDetailFragment(mRecipe, recipeDetailFragment);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isTablet()){
            outState.putParcelable(INTENT_PARCEL_STEP, mStep);
        }
    }

    public boolean isTablet() {
        return mTabletMode;
    }

    private void replaceDetailFragment(Recipe recipe, RecipeDetailFragment recipeDetailFragment) {
        if (recipeDetailFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(INTENT_PARCEL_RECIPE, recipe);
            bundle.putBoolean(INTENT_BOOL_TABLET_MODE, mTabletMode);
            recipeDetailFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_detail_container, recipeDetailFragment, RecipeDetailFragment.TAG).
                commit();
    }

    private void replaceStepFragment(int position, RecipeStepFragment recipeStepFragment) {
        if (recipeStepFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            mStep = mRecipe.getSteps().get(position);
            bundle.putParcelable(INTENT_PARCEL_RECIPE, mRecipe);
            bundle.putParcelable(INTENT_PARCEL_STEP, mStep);
            bundle.putBoolean(INTENT_BOOL_TABLET_MODE, mTabletMode);
            recipeStepFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_step_container, recipeStepFragment, RecipeStepFragment.TAG).
                commit();
    }

    private void launchStepActivity(int position) {
        Intent intent = new Intent(this, RecipeStepActivity.class);
        mStep = mRecipe.getSteps().get(position);
        mStep.setPosition(position);
        intent.putExtra(INTENT_PARCEL_RECIPE, mRecipe);
        intent.putExtra(INTENT_PARCEL_STEP, mStep);
        intent.putExtra(INTENT_BOOL_TABLET_MODE, mTabletMode);
        startActivity(intent);
    }

    @Override
    public void handleClick(int position) {
        if (isTablet()) {
            replaceStepFragment(position, new RecipeStepFragment());
        }else{
            launchStepActivity(position);
        }
    }
}
