package com.khbtravel.ardianys.dapoor.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.RecipeListActivity;
import com.khbtravel.ardianys.dapoor.RecipeStepActivity;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ardianys on 8/27/17.
 */

public class RecipeStepFragment extends Fragment {

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
    long videoSeekAt;
    public static final String VIDEO_SEEK_AT = "VIDEO_SEEK_AT";
    public static final String TAG = "RecipeStepFragment";
    private Boolean mTabletMode = false;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        Log.e(TAG, videoSeekAt + " onCreateView");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE);
            step = bundle.getParcelable(RecipeListActivity.INTENT_PARCEL_STEP);
            mTabletMode = bundle.getBoolean(RecipeListActivity.INTENT_BOOL_TABLET_MODE, false);
        }

        if (savedInstanceState != null) {
            videoSeekAt = savedInstanceState.getLong(VIDEO_SEEK_AT, 0);
        }

        if (mTabletMode){
            mButtonNext.setVisibility(View.GONE);
            mButtonPrevious.setVisibility(View.GONE);
        } else {
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
        }

        mTvDescription.setText(step.getDescription());

        if (step.getVideoURL().isEmpty()){
            mPlayerView.setVisibility(View.GONE);
        } else {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }

        if (step.getThumbnailURL().isEmpty()){
            mImageViewThumbnail.setVisibility(View.GONE);
        } else {
            Picasso.with(mImageViewThumbnail.getContext())
                    .load(step.getThumbnailURL())
                    .into(mImageViewThumbnail);
        }

        return rootView;
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, recipe);
        if (mExoPlayer != null){
            videoSeekAt = mExoPlayer.getCurrentPosition();
            outState.putLong(VIDEO_SEEK_AT, videoSeekAt);
        } else if (videoSeekAt > 0){
            outState.putLong(VIDEO_SEEK_AT, videoSeekAt);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), "Dapoor");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(videoSeekAt);
            Log.e(TAG, videoSeekAt + " seek at");
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null){
            videoSeekAt = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }
}
