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
import com.khbtravel.ardianys.dapoor.ui.StepAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.ItemClickListener {

    @BindView(R.id.tv_recipe_ingredients)
    TextView mTextViewIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;

    StepAdapter stepAdapter;

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

        // Get Intent and check for recipe ID that added in intent's extra
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("PARCEL_RECIPE")) {
                Recipe recipe = intent.getParcelableExtra("PARCEL_RECIPE");
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
        }
    }

    @Override
    public void onStepClick(View view, int position) {

    }
}
