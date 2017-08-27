package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.adapter.RecipeAdapter;
import com.khbtravel.ardianys.dapoor.utils.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListActivity extends AppCompatActivity  implements RecipeAdapter.ItemClickListener {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String INTENT_PARCEL_RECIPE = "PARCEL_RECIPE";
    public static final String INTENT_PARCEL_STEP = "PARCEL_STEP";

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;

    RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();

                mRecipeAdapter.setRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipeListActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onRecipeClick(View view, int position) {
        Intent intentToStartDetailActivity = new Intent(this, RecipeDetailActivity.class);
        Parcelable recipeParcel = mRecipeAdapter.getRecipe(position);
        intentToStartDetailActivity.putExtra(INTENT_PARCEL_RECIPE, recipeParcel);
        startActivity(intentToStartDetailActivity);
    }
}
