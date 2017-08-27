package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RecipeStepActivity extends AppCompatActivity {

    Recipe recipe;
    Step step;
    long videoSeekAt = 0;
    public static final String VIDEO_SEEK_AT = "VIDEO_SEEK_AT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE);
            step = savedInstanceState.getParcelable(RecipeListActivity.INTENT_PARCEL_STEP);
            videoSeekAt = savedInstanceState.getLong(VIDEO_SEEK_AT, 0);
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RecipeListActivity.INTENT_PARCEL_RECIPE)) {
                recipe = intent.getParcelableExtra(RecipeListActivity.INTENT_PARCEL_RECIPE);
            }
            if (intent.hasExtra(RecipeListActivity.INTENT_PARCEL_STEP)) {
                step = intent.getParcelableExtra(RecipeListActivity.INTENT_PARCEL_STEP);
            }
        }

        replaceFragment(step);
    }

    private void replaceFragment(Step step) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        bundle.putParcelable(RecipeListActivity.INTENT_PARCEL_STEP, step);

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.recipe_step_container, recipeStepFragment).
                commit();
    }

    public void clickPrevious(View view){
        replaceFragment((Step) view.getTag());

    }

    public void clickNext(View view){
        replaceFragment((Step) view.getTag());
    }

}
