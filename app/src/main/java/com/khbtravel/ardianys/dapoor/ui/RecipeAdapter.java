package com.khbtravel.ardianys.dapoor.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;

import android.widget.TextView;

import java.util.ArrayList;
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
        View view = mInflater.inflate(R.layout.list_recipe_item, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipesData.get(position);
        holder.mRecipeTitleTv.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipesData) return 0;
        return mRecipesData.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_title) TextView mRecipeTitleTv;

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