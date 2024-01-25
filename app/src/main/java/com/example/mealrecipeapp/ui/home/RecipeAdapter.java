package com.example.mealrecipeapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.data.remote.response.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return (recipes != null) ? recipes.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView posterImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titileTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
        }

        public void bind(Recipe recipe) {
            titleTextView.setText(recipe.getTitle());
            Glide.with(itemView)
                    .load(recipe.getImage())
                    .into(posterImageView);
        }
    }
}
