package com.example.mealrecipeapp.ui.recipeinformation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealrecipeapp.R
import com.example.mealrecipeapp.data.remote.response.Ingredient
import com.example.mealrecipeapp.databinding.RecipeInformationItemBinding

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.RecipeViewHolder>() {
    private var ingredients: List<Ingredient> = listOf()
    fun setIngredients(ingredients: List<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            RecipeInformationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class RecipeViewHolder(private val binding: RecipeInformationItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(ingredient: Ingredient) {
            binding.numberTextView.text = itemView.resources.getString(R.string.position, (adapterPosition + 1).toString())
            binding.informationTextView.text = itemView.resources.getString(
                R.string.ingredient_information,
                ingredient.amount.toString(),
                ingredient.unit,
                ingredient.name
            )
        }
    }
}
