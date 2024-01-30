package com.example.mealrecipeapp.ui.favorite;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.data.local.entity.RecipeEntity;
import com.example.mealrecipeapp.databinding.RecipeItemBinding;

import java.util.List;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.RecipeViewHolder> {

    private final OnClickListener onClickListener;
    private List<RecipeEntity> recipes;

    public FavoriteRecipeAdapter(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setRecipes(List<RecipeEntity> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeItemBinding view = RecipeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeEntity recipe = recipes.get(position);
        holder.bind(recipe, onClickListener);
    }

    @Override
    public int getItemCount() {
        return (recipes != null) ? recipes.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final RecipeItemBinding binding;

        public RecipeViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RecipeEntity recipe, OnClickListener onClickListener) {
            binding.titileTextView.setText(recipe.getTitle());
            binding.voteTextView.setText(recipe.getAggregateLikes() + " likes");
            binding.timeTextView.setText(recipe.getReadyInMinutes() + " min");
            binding.servingTextView.setText(recipe.getServings() + " servings");
            Glide.with(itemView)
                    .load(recipe.getImage())
                    .into(binding.posterImageView);

            binding.getRoot().setOnClickListener(v -> onClickListener.onItemClick(recipe));
        }
    }
    // Interface for handling item clicks
    public interface OnClickListener {
        void onItemClick(RecipeEntity recipe);
    }
}
