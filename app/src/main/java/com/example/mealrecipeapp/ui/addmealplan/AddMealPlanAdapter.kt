package com.example.mealrecipeapp.ui.addmealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealrecipeapp.data.remote.response.Recipe
import com.example.mealrecipeapp.databinding.AddMealPlanItemBinding

class AddMealPlanAdapter(private val addClickListener: (Recipe) -> Unit) :
    RecyclerView.Adapter<AddMealPlanAdapter.RecipeViewHolder>() {
    private var recipes: List<Recipe> = listOf()
    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            AddMealPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe, addClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class RecipeViewHolder(private val binding: AddMealPlanItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(recipe: Recipe, addClickListener: (Recipe) -> Unit) {
            binding.titileTextView.text = recipe.title
            Glide.with(itemView)
                .load(recipe.image)
                .into(binding.posterImageView)
            binding.addMealPlanButton.setOnClickListener {
                addClickListener(recipe)
            }
        }
    }
}
