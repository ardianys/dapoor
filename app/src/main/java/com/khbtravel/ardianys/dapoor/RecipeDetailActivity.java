package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.khbtravel.ardianys.dapoor.fragment.MasterListInterface;
import com.khbtravel.ardianys.dapoor.fragment.RecipeDetailFragment;
import com.khbtravel.ardianys.dapoor.fragment.RecipeStepFragment;
import com.khbtravel.ardianys.dapoor.pojo.Ingredient;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.khbtravel.ardianys.dapoor.adapter.StepAdapter;
import com.khbtravel.ardianys.dapoor.widget.RecipeWidgetService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_BOOL_TABLET_MODE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_STEP;

public class RecipeDetailActivity extends AppCompatActivity
        implements MasterListInterface {

    private Boolean mTabletMode = false;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(INTENT_PARCEL_RECIPE);
        }

        // Get Intent and check for recipe ID that added in intent's extra
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_PARCEL_RECIPE)) {
                recipe = intent.getParcelableExtra(INTENT_PARCEL_RECIPE);
            }
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SHARED_PREFS_INGREDIENTS", recipe.buildIngredients());
        editor.commit();

        RecipeWidgetService.startActionUpdateWidgets(getApplicationContext(), recipe);


        if(findViewById(R.id.recipe_step_container)!= null){
            mTabletMode = true;
        }

        if (isTablet()){
            if(savedInstanceState == null) {
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                replaceFragment(0, recipeStepFragment);
            } else {
                RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().
                        findFragmentByTag(RecipeStepFragment.TAG);
                replaceFragment(0, recipeStepFragment);
            }
        }

        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(INTENT_PARCEL_RECIPE, recipe);
        bundle.putBoolean(INTENT_BOOL_TABLET_MODE, mTabletMode);
        recipeDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_detail_container, recipeDetailFragment).
                commit();

    }

    public boolean isTablet() {
        return mTabletMode;
    }

    private void replaceFragment(int position, RecipeStepFragment recipeStepFragment) {
        if (recipeStepFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            Step step = recipe.getSteps().get(position);
            bundle.putParcelable(INTENT_PARCEL_RECIPE, recipe);
            bundle.putParcelable(INTENT_PARCEL_STEP, step);
            bundle.putBoolean(INTENT_BOOL_TABLET_MODE, mTabletMode);
            recipeStepFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_step_container, recipeStepFragment, RecipeStepFragment.TAG).
                commit();
    }

    private void launchStepActivity(int position) {
        Intent intent = new Intent(this, RecipeStepActivity.class);
        Step step = recipe.getSteps().get(position);
        step.setPosition(position);
        intent.putExtra(INTENT_PARCEL_RECIPE, recipe);
        intent.putExtra(INTENT_PARCEL_STEP, step);
        intent.putExtra(INTENT_BOOL_TABLET_MODE, mTabletMode);
        startActivity(intent);
    }

    @Override
    public void handleClick(int position) {
        if (isTablet()) {
            replaceFragment(position, new RecipeStepFragment());
        }else{
            launchStepActivity(position);
        }
    }
}
