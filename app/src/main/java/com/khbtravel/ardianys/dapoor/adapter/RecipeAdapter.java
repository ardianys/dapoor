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

import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ardianys on 8/25/17.
 */


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipesData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecipeAdapter(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.rv_recipe_list_item, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipesData.get(position);
        holder.mRecipeTitleTv.setText(recipe.getName());

        if (!recipe.getImage().isEmpty()){
            Picasso.with(holder.mRecipeThumbIv.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.menu)
                    .error(R.drawable.menu)
                    .into(holder.mRecipeThumbIv);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipesData) return 0;
        return mRecipesData.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_title)
        TextView mRecipeTitleTv;

        @BindView(R.id.iv_recipe_menu_list)
        ImageView mRecipeThumbIv;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onRecipeClick(view, getAdapterPosition());
        }
    }

    public Recipe getRecipe(int id) {
        return mRecipesData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onRecipeClick(View view, int position);
    }

    public void setRecipes(List<Recipe> recipesData) {
        mRecipesData = recipesData;
        notifyDataSetChanged();
    }
}