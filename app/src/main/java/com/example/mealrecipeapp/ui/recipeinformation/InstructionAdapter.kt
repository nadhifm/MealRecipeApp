package com.example.mealrecipeapp.ui.recipeinformation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealrecipeapp.data.remote.response.Instruction
import com.example.mealrecipeapp.databinding.RecipeInformationItemBinding

class InstructionAdapter : RecyclerView.Adapter<InstructionAdapter.RecipeViewHolder>() {
    private var instructions: List<Instruction> = listOf()
    fun setInstructions(instructions: List<Instruction>) {
        this.instructions = instructions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            RecipeInformationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val instruction = instructions[position]
        holder.bind(instruction)
    }

    override fun getItemCount(): Int {
        return instructions.size
    }

    class RecipeViewHolder(private val binding: RecipeInformationItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(instruction: Instruction) {
            binding.numberTextView.text = instruction.number.toString()
            binding.informationTextView.text = instruction.step
        }
    }
}
