package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.khbtravel.ardianys.dapoor.pojo.Ingredient;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.khbtravel.ardianys.dapoor.adapter.StepAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.ItemClickListener {

    @BindView(R.id.tv_recipe_ingredients)
    TextView mTextViewIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;

    StepAdapter stepAdapter;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));

        stepAdapter = new StepAdapter(this);
        stepAdapter.setClickListener(this);
        mRecyclerViewSteps.setAdapter(stepAdapter);
        mRecyclerViewSteps.setHasFixedSize(true);


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

        String output = "";
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        for(int i=0; i< ingredients.size(); i++){
            Ingredient ingredient = ingredients.get(i);
            if (ingredient != null ){
                output += ingredient.getQuantity() + " " +
                        ingredient.getMeasure() + " " +
                        ingredient.getIngredient() + "\n";
            }
        }
        mTextViewIngredients.setText(output);
        stepAdapter.setSteps(recipe.getSteps());
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
    }

    @Override
    public void onStepClick(View view, int position) {
        Intent intentToStartDetailActivity = new Intent(this, RecipeStepActivity.class);
        Step step = stepAdapter.getStep(position);
        step.setPosition(position);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_STEP, step);
        startActivity(intentToStartDetailActivity);
    }
}
