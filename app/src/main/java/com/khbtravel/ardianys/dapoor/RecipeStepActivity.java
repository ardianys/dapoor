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
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {

    @BindView(R.id.b_next_step)
    Button mButtonNext;

    @BindView(R.id.b_previous_step)
    Button mButtonPrevious;

    SimpleExoPlayer mExoPlayer;

    @BindView(R.id.ep_player_view)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_step_description)
    TextView mTvDescription;

    @BindView(R.id.iv_step_thumbnail)
    ImageView mImageViewThumbnail;

    Recipe recipe;
    Step step;
    long videoSeekAt = 0;
    public static final String VIDEO_SEEK_AT = "VIDEO_SEEK_AT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

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

        mTvDescription.setText(step.getDescription());
        /**
         * The step ID in API isn't using incremental value,
         * we should add new variable to store the position
         */
        if (step.getPosition() < recipe.getSteps().size()-1){
            Step stepNext = recipe.getSteps().get(step.getPosition()+1);
            stepNext.setPosition(step.getPosition()+1);
            mButtonNext.setTag(stepNext);
        } else {
            mButtonNext.setVisibility(View.GONE);
        }
        if (step.getPosition() > 0){
            Step stepPrevious = recipe.getSteps().get(step.getPosition()-1);
            stepPrevious.setPosition(step.getPosition()-1);
            mButtonPrevious.setTag(stepPrevious);
        } else {
            mButtonPrevious.setVisibility(View.GONE);
        }

        if (!step.getVideoURL().isEmpty()){
            initializePlayer(Uri.parse(step.getVideoURL()));
        } else {
            mPlayerView.setVisibility(View.GONE);
        }

        if (!step.getThumbnailURL().isEmpty()){
            Picasso.with(mImageViewThumbnail.getContext())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_error)
                    .into(mImageViewThumbnail);
        } else {
            mImageViewThumbnail.setVisibility(View.GONE);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        if (mExoPlayer != null){
            videoSeekAt = mExoPlayer.getCurrentPosition();
            outState.putLong(VIDEO_SEEK_AT, videoSeekAt);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "Dapoor");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(videoSeekAt);
        }
    }

    public void clickPrevious(View view){
        releasePlayer();
        Intent intentToStartDetailActivity = new Intent(this, RecipeStepActivity.class);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_STEP, (Step) view.getTag());
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        startActivity(intentToStartDetailActivity);

    }

    public void clickNext(View view){
        releasePlayer();
        Intent intentToStartDetailActivity = new Intent(this, RecipeStepActivity.class);
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_STEP, (Step) view.getTag());
        intentToStartDetailActivity.putExtra(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        startActivity(intentToStartDetailActivity);
    }

    private void releasePlayer() {
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
