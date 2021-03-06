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

package com.khbtravel.ardianys.dapoor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.fragment.MasterListInterface;
import com.khbtravel.ardianys.dapoor.pojo.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ardianys on 8/25/17.
 */


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> mStepsData;
    private LayoutInflater mInflater;
    private MasterListInterface mClickListener;

    public StepAdapter(MasterListInterface itemClickListener) {
        mClickListener = itemClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.rv_recipe_step_item, parent, false);
        StepViewHolder stepViewHolder = new StepViewHolder(view);
        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {
        Step step = mStepsData.get(position);
        holder.mTvStepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (null == mStepsData) return 0;
        return mStepsData.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description) TextView mTvStepShortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.handleClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.handleClick(getAdapterPosition());
        }
    }

    public Step getStep(int id) {
        return mStepsData.get(id);
    }

    public void setSteps(ArrayList<Step> stepsData) {
        mStepsData = stepsData;
        notifyDataSetChanged();
    }
}