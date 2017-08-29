package com.khbtravel.ardianys.dapoor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.RecipeListActivity;
import com.khbtravel.ardianys.dapoor.adapter.StepAdapter;
import com.khbtravel.ardianys.dapoor.pojo.Ingredient;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ardianys on 8/27/17.
 */

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.tv_recipe_ingredients)
    TextView mTextViewIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;

    @BindView(R.id.iv_recipe_thumbnail)
    ImageView mImageViewRecipeThumb;

    StepAdapter stepAdapter;

    Recipe recipe;
    private MasterListInterface listener;


    public static RecipeDetailFragment newInstance(MasterListInterface listener) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.listener = listener;
        return recipeDetailFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepAdapter = new StepAdapter(listener);
        mRecyclerViewSteps.setAdapter(stepAdapter);
        mRecyclerViewSteps.setHasFixedSize(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE);
        }

        mTextViewIngredients.setText(recipe.buildIngredients());
        stepAdapter.setSteps(recipe.getSteps());

        if (recipe.getImage().isEmpty()){
            mImageViewRecipeThumb.setVisibility(View.GONE);
        } else {
            Picasso.with(mImageViewRecipeThumb.getContext())
                    .load(recipe.getImage())
                    .into(mImageViewRecipeThumb);
        }

        return rootView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
    }
}
