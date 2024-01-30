package com.example.mealrecipeapp.ui.recipeinformation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealrecipeapp.data.remote.response.Instruction;
import com.example.mealrecipeapp.databinding.RecipeInformationItemBinding;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.RecipeViewHolder> {

    private List<Instruction> instructions;

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
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
        Instruction instruction = instructions.get(position);
        holder.bind(instruction);
    }

    @Override
    public int getItemCount() {
        return (instructions != null) ? instructions.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final RecipeInformationItemBinding binding;

        public RecipeViewHolder(RecipeInformationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Instruction instruction) {
            binding.numberTextView.setText(String.valueOf(instruction.getNumber()));
            binding.informationTextView.setText(instruction.getStep());
        }
    }
}
