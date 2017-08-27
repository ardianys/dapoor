package com.khbtravel.ardianys.dapoor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.khbtravel.ardianys.dapoor.pojo.Ingredient;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.INTENT_PARCEL_STEP)) {
                Step step = intent.getParcelableExtra(MainActivity.INTENT_PARCEL_STEP);
                mTvDescription.setText(step.getDescription());
                if (!step.getVideoURL().isEmpty()){

                    // Load the question mark as the background image until the user answers the question.
                    mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                            (getResources(), R.drawable.question_mark));

                    initializePlayer(Uri.parse(step.getVideoURL()));
                } else {
                    mPlayerView.setVisibility(View.GONE);
                }
            }
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
        }
    }

    private void clickPrevious(View view){
        mExoPlayer.stop();
    }

    private void clickNext(View view){
        mExoPlayer.stop();
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerView.getVisibility() == View.VISIBLE){
            releasePlayer();
        }
    }
}
