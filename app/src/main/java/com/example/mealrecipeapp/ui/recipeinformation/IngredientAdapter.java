package com.example.mealrecipeapp.ui.recipeinformation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.data.remote.response.Ingredient;
import com.example.mealrecipeapp.databinding.AddMealPlanItemBinding;
import com.example.mealrecipeapp.databinding.RecipeInformationItemBinding;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.RecipeViewHolder> {

    private List<Ingredient> ingredients;

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeInformationItemBinding view = RecipeInformationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return (ingredients != null) ? ingredients.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final RecipeInformationItemBinding binding;

        public RecipeViewHolder(RecipeInformationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient ingredient) {
            binding.numberTextView.setText(String.valueOf(getAdapterPosition()+1));
            binding.informationTextView.setText(ingredient.getAmount() + " " + ingredient.getUnit() + " " + ingredient.getName());
        }
    }
}
