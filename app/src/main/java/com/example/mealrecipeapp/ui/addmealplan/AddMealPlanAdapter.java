package com.example.mealrecipeapp.ui.addmealplan;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.databinding.AddMealPlanItemBinding;

import java.util.List;

public class AddMealPlanAdapter extends RecyclerView.Adapter<AddMealPlanAdapter.RecipeViewHolder> {

    private final OnAddClickListener onAddClickListener;
    private List<Recipe> recipes;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public AddMealPlanAdapter(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddMealPlanItemBinding view = AddMealPlanItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, onAddClickListener);
    }

    @Override
    public int getItemCount() {
        return (recipes != null) ? recipes.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final AddMealPlanItemBinding binding;

        public RecipeViewHolder(AddMealPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Recipe recipe, OnAddClickListener onAddClickListener) {
            binding.titileTextView.setText(recipe.getTitle());
            Glide.with(itemView)
                    .load(recipe.getImage())
                    .into(binding.posterImageView);
            binding.addMealPlanButton.setOnClickListener(onClick -> onAddClickListener.onItemClick(recipe));
        }
    }

    // Interface for handling item clicks
    public interface OnAddClickListener {
        void onItemClick(Recipe recipe);
    }
}
