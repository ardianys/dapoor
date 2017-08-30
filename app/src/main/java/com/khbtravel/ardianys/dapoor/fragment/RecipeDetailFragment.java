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
import com.khbtravel.ardianys.dapoor.adapter.StepAdapter;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.khbtravel.ardianys.dapoor.RecipeDetailActivity.RECYCLER_VIEW_STEP;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.tv_recipe_ingredients)
    TextView mTextViewIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;

    @BindView(R.id.iv_recipe_thumbnail)
    ImageView mImageViewRecipeThumb;

    private StepAdapter mStepAdapter;
    private Recipe mRecipe;
    private MasterListInterface mListener;

    public static final String TAG = "RecipeDetailFragment";

    public static RecipeDetailFragment newInstance(MasterListInterface listener) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.mListener = listener;
        return recipeDetailFragment;
    }

    public void setListener(MasterListInterface listener){
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        mStepAdapter = new StepAdapter(mListener);
        mRecyclerViewSteps.setAdapter(mStepAdapter);
        mRecyclerViewSteps.setHasFixedSize(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipe = bundle.getParcelable(INTENT_PARCEL_RECIPE);
        }

        mTextViewIngredients.setText(mRecipe.buildIngredients());
        mStepAdapter.setSteps(mRecipe.getSteps());

        if (mRecipe.getImage().isEmpty()){
            mImageViewRecipeThumb.setVisibility(View.GONE);
        } else {
            Picasso.with(mImageViewRecipeThumb.getContext())
                    .load(mRecipe.getImage())
                    .into(mImageViewRecipeThumb);
        }

        if (savedInstanceState != null){
            mRecyclerViewSteps.getLayoutManager().onRestoreInstanceState(
                    savedInstanceState.getParcelable(RECYCLER_VIEW_STEP)
            );
        }

        return rootView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INTENT_PARCEL_RECIPE, mRecipe);
        outState.putParcelable(RECYCLER_VIEW_STEP, mRecyclerViewSteps.getLayoutManager().onSaveInstanceState());
    }
}
