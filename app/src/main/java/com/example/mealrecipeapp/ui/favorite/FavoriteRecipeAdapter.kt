package com.example.mealrecipeapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealrecipeapp.R
import com.example.mealrecipeapp.data.local.entity.RecipeEntity
import com.example.mealrecipeapp.databinding.RecipeItemBinding

class FavoriteRecipeAdapter(
    private val clickListener: (Long) -> Unit
) : RecyclerView.Adapter<FavoriteRecipeAdapter.RecipeViewHolder>() {
    private var recipes: List<RecipeEntity> = listOf()
    fun setRecipes(recipes: List<RecipeEntity>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe, clickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class RecipeViewHolder(private val binding: RecipeItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(recipe: RecipeEntity, clickListener: (Long) -> Unit) {
            binding.titileTextView.text = recipe.title
            binding.voteTextView.text = itemView.resources.getString(R.string.like_counts, recipe.aggregateLikes.toString())
            binding.timeTextView.text = itemView.resources.getString(R.string.time_counts, recipe.readyInMinutes.toString())
            binding.servingTextView.text = itemView.resources.getString(R.string.serving_counts, recipe.servings.toString())
            Glide.with(itemView)
                .load(recipe.image)
                .into(binding.posterImageView)
            binding.root.setOnClickListener { clickListener(recipe.id) }
        }
    }
}
