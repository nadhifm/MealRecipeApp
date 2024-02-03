package com.example.mealrecipeapp.ui.mealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealrecipeapp.data.remote.response.MealPlan
import com.example.mealrecipeapp.databinding.MealPlanItemBinding
import com.example.mealrecipeapp.ui.mealplan.MealPlanAdapter.MealPlanViewHolder

class MealPlanAdapter(
    private val clickListener: (Long) -> Unit,
    private val deleteClickListener: (Long) -> Unit,
) : RecyclerView.Adapter<MealPlanViewHolder>() {
    private var mealPlans: List<MealPlan> = listOf()
    fun setMealPlans(mealPlans: List<MealPlan>) {
        this.mealPlans = mealPlans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        val view = MealPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        val mealPlan = mealPlans[position]
        holder.bind(mealPlan, clickListener, deleteClickListener)
    }

    override fun getItemCount(): Int {
        return mealPlans.size
    }

    class MealPlanViewHolder(private val binding: MealPlanItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(
            mealPlan: MealPlan,
            clickListener: (Long) -> Unit,
            deleteClickListener: (Long) -> Unit
        ) {
            binding.titileTextView.text = mealPlan.value.title
            Glide.with(itemView)
                .load(mealPlan.value.image)
                .into(binding.posterImageView)
            binding.deleteButton.setOnClickListener {
                deleteClickListener(
                    mealPlan.id
                )
            }
            binding.root.setOnClickListener { clickListener(mealPlan.value.id) }
        }
    }
}
