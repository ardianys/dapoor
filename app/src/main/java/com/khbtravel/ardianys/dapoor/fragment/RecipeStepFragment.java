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
*/

package com.khbtravel.ardianys.dapoor.fragment;

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
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.khbtravel.ardianys.dapoor.pojo.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.isEmpty;

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

    private Recipe mRecipe;
    private Step mStep;
    private long mVideoSeekAt;
    private Boolean mTabletMode = false;

    public static final String VIDEO_SEEK_AT = "VIDEO_SEEK_AT";
    public static final String TAG = "RecipeStepFragment";

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, mVideoSeekAt + " onResume");
        if (!isEmpty(mStep.getVideoURL())){
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        Log.e(TAG, mVideoSeekAt + " onCreateView");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipe = bundle.getParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE);
            mStep = bundle.getParcelable(RecipeListActivity.INTENT_PARCEL_STEP);
            mTabletMode = bundle.getBoolean(RecipeListActivity.INTENT_BOOL_TABLET_MODE, false);
        }

        if (savedInstanceState != null) {
            mVideoSeekAt = savedInstanceState.getLong(VIDEO_SEEK_AT, 0);
        }

        if (mTabletMode){
            mButtonNext.setVisibility(View.GONE);
            mButtonPrevious.setVisibility(View.GONE);
        } else {
            /**
             * The Step ID in API isn't using incremental value,
             * we should add new variable to store the position
             */
            if (mStep.getPosition() < mRecipe.getSteps().size()-1){
                Step stepNext = mRecipe.getSteps().get(mStep.getPosition()+1);
                stepNext.setPosition(mStep.getPosition()+1);
                mButtonNext.setTag(stepNext);
            } else {
                mButtonNext.setVisibility(View.GONE);
            }
            if (mStep.getPosition() > 0){
                Step stepPrevious = mRecipe.getSteps().get(mStep.getPosition()-1);
                stepPrevious.setPosition(mStep.getPosition()-1);
                mButtonPrevious.setTag(stepPrevious);
            } else {
                mButtonPrevious.setVisibility(View.GONE);
            }
        }

        mTvDescription.setText(mStep.getDescription());

        if (isEmpty(mStep.getVideoURL())){
            mPlayerView.setVisibility(View.GONE);
        } else {
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }

        if (isEmpty(mStep.getThumbnailURL())){
            mImageViewThumbnail.setVisibility(View.GONE);
        } else {
            Picasso.with(mImageViewThumbnail.getContext())
                    .load(mStep.getThumbnailURL())
                    .into(mImageViewThumbnail);
        }

        return rootView;
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeListActivity.INTENT_PARCEL_RECIPE, mRecipe);
        if (mExoPlayer != null){
            mVideoSeekAt = mExoPlayer.getCurrentPosition();
            outState.putLong(VIDEO_SEEK_AT, mVideoSeekAt);
        } else if (mVideoSeekAt > 0){
            outState.putLong(VIDEO_SEEK_AT, mVideoSeekAt);
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
            mExoPlayer.seekTo(mVideoSeekAt);
            Log.e(TAG, mVideoSeekAt + " seek at (initialize)");
            mExoPlayer.setPlayWhenReady(true);
        } else {
            Log.e(TAG, mVideoSeekAt + " seek at (exo player is not null)");
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null){
            mVideoSeekAt = mExoPlayer.getCurrentPosition();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }
}
