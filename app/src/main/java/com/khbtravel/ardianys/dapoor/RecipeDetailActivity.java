package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
        implements MasterListInterface {

    private Boolean mTabletMode = false;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE);
        }

        // Get Intent and check for recipe ID that added in intent's extra
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RecipeListActivity.INTENT_PARCEL_RECIPE)) {
                recipe = intent.getParcelableExtra(RecipeListActivity.INTENT_PARCEL_RECIPE);
            }
        }

        if(findViewById(R.id.recipe_step_container)!= null){
            mTabletMode = true;
            replaceFragment(0);
        }

        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        bundle.putBoolean(RecipeListActivity.INTENT_BOOL_TABLET_MODE, mTabletMode);
        recipeDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_detail_container, recipeDetailFragment).
                commit();

    }

    public boolean isTablet() {
        return mTabletMode;
    }

    private void replaceFragment(int position) {
        Bundle bundle = new Bundle();
        Step step = recipe.getSteps().get(position);

        bundle.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        bundle.putParcelable(RecipeListActivity.INTENT_PARCEL_STEP, step);
        bundle.putBoolean(RecipeListActivity.INTENT_BOOL_TABLET_MODE, mTabletMode);

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_step_container, recipeStepFragment).
                commit();
    }

    private void launchStepActivity(int position) {
        Intent intentToStartDetailActivity = new Intent(this, RecipeStepActivity.class);
        Step step = recipe.getSteps().get(position);
        step.setPosition(position);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_STEP, step);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void handleClick(int position) {
        if (isTablet()) {
            replaceFragment(position);
        }else{
            launchStepActivity(position);
        }
    }
}
