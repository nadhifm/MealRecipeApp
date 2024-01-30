package com.example.mealrecipeapp.ui.mealplan;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.databinding.MealPlanItemBinding;

import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {

    private final OnClickListener onDeleteClickListener;
    private final OnClickListener onClickListener;
    private List<MealPlan> mealPlans;

    public void setMealPlans(List<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
        notifyDataSetChanged();
    }

    public MealPlanAdapter(OnClickListener onClickListener, OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MealPlanItemBinding view = MealPlanItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MealPlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        MealPlan mealPlan = mealPlans.get(position);
        holder.bind(mealPlan, onDeleteClickListener, onClickListener);
    }

    @Override
    public int getItemCount() {
        return (mealPlans != null) ? mealPlans.size() : 0;
    }

    public static class MealPlanViewHolder extends RecyclerView.ViewHolder {
        private final MealPlanItemBinding binding;

        public MealPlanViewHolder(MealPlanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MealPlan mealPlan, OnClickListener onDeleteClickListener, OnClickListener onClickListener) {
            binding.titileTextView.setText(mealPlan.getValue().getTitle());
            Glide.with(itemView)
                    .load(mealPlan.getValue().getImage())
                    .into(binding.posterImageView);
            binding.deleteButton.setOnClickListener(view -> onDeleteClickListener.onItemClick(mealPlan));
            binding.getRoot().setOnClickListener(view -> onClickListener.onItemClick(mealPlan));
        }
    }

    // Interface for handling item clicks
    public interface OnClickListener {
        void onItemClick(MealPlan mealPlan);
    }
}
