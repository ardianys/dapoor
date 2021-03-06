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
import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.adapter.RecipeAdapter;
import com.khbtravel.ardianys.dapoor.utils.ApiClient;
import com.khbtravel.ardianys.dapoor.IdlingResource.SimpleIdlingResource;

public class RecipeListActivity extends AppCompatActivity  implements RecipeAdapter.ItemClickListener {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @Nullable
    private SimpleIdlingResource mIdlingResource;
    private RecipeAdapter mRecipeAdapter;

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String INTENT_PARCEL_RECIPE = "PARCEL_RECIPE";
    public static final String INTENT_PARCEL_STEP = "PARCEL_STEP";
    public static final String INTENT_BOOL_TABLET_MODE = "BOOL_TABLET_MODE";
    public static final String RECYCLER_VIEW_RECIPE = "RECYCLER_VIEW_RECIPE";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mRecipeAdapter = new RecipeAdapter(RecipeListActivity.this);
        mRecipeAdapter.setClickListener(RecipeListActivity.this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setHasFixedSize(true);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<List<Recipe>> call = client.getRecipes();

        // Espresso Testing
        getIdlingResource();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                mProgressBar.setVisibility(View.GONE);
                mRecipeAdapter.setRecipes(recipes);

                if (savedInstanceState != null){
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(
                            savedInstanceState.getParcelable(RECYCLER_VIEW_RECIPE)
                    );
                }
                mIdlingResource.setIdleState(true);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                CoordinatorLayout coordinatorLayout =
                        (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Oups, sorry. Error occurred :(", Snackbar.LENGTH_LONG);
                snackbar.show();

                mIdlingResource.setIdleState(true);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(RECYCLER_VIEW_RECIPE, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onRecipeClick(View view, int position) {
        Intent intentToStartDetailActivity = new Intent(this, RecipeDetailActivity.class);
        Parcelable recipeParcel = mRecipeAdapter.getRecipe(position);
        intentToStartDetailActivity.putExtra(INTENT_PARCEL_RECIPE, recipeParcel);
        startActivity(intentToStartDetailActivity);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
