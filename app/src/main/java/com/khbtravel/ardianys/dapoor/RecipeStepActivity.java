package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.khbtravel.ardianys.dapoor.fragment.RecipeDetailFragment;
import com.khbtravel.ardianys.dapoor.fragment.RecipeStepFragment;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
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
