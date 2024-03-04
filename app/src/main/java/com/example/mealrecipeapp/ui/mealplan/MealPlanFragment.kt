package com.example.mealrecipeapp.ui.mealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentMealPlanBinding
import com.example.mealrecipeapp.utils.Resource
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealPlanFragment : Fragment() {
    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealPlanViewModel: MealPlanViewModel
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var breakfastMealPlanAdapter: MealPlanAdapter
    private lateinit var lunchMealPlanAdapter: MealPlanAdapter
    private lateinit var dinnerMealPlanAdapter: MealPlanAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mealPlanViewModel.getMealPlans(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        mealPlanViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[MealPlanViewModel::class.java]
        binding.dateTextView.setOnClickListener {
            val dialogDatePicker = requireActivity().supportFragmentManager.findFragmentByTag("date-picker")
            if (dialogDatePicker == null) {
                datePicker.show(
                    requireActivity().supportFragmentManager,
                    "date-picker"
                )
            }
        }
        binding.leftButton.setOnClickListener { v: View? -> mealPlanViewModel.setPrevDate() }
        binding.rightButton.setOnClickListener { v: View? -> mealPlanViewModel.setNextDate() }
        setupRecyclerView()
        observeDate()
        observeDeleteMelaPlanResult()
        observeMessageError()
        observeMealPlans()
    }

    private fun setupRecyclerView() {
        breakfastMealPlanAdapter = MealPlanAdapter(
            { mealPlanId ->
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(
                        mealPlanId
                    )
                )
            }
        ) { mealPlanId -> mealPlanViewModel.deleteMealPlan(mealPlanId) }
        binding.breakfastRecyclerView.adapter = breakfastMealPlanAdapter
        lunchMealPlanAdapter = MealPlanAdapter(
            { mealPlanId ->
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(
                        mealPlanId
                    )
                )
            }
        ) { mealPlanId -> mealPlanViewModel.deleteMealPlan(mealPlanId) }
        binding.lunchRecyclerView.adapter = lunchMealPlanAdapter
        dinnerMealPlanAdapter = MealPlanAdapter(
            { mealPlanId ->
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(
                        mealPlanId
                    )
                )
            }
        ) { mealPlanId -> mealPlanViewModel.deleteMealPlan(mealPlanId) }
        binding.dinnerRecyclerView.adapter = dinnerMealPlanAdapter
    }

    private fun observeDate() {
        mealPlanViewModel.date.observe(viewLifecycleOwner) { date ->
            val dateFormat = "E, dd LLL yyyy"
            val selectedDate = SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
            binding.dateTextView.text = selectedDate
            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(date.time)
                .build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                mealPlanViewModel.setDate(
                    Date(
                        selection
                    )
                )
            }
            binding.addBreakfastButton.setOnClickListener {
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(
                        1,
                        date.time
                    )
                )
            }
            binding.addLunchButton.setOnClickListener {
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(
                        2,
                        date.time
                    )
                )
            }
            binding.addDinnerButton.setOnClickListener {
                findNavController().navigate(
                    MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(
                        3,
                        date.time
                    )
                )
            }
        }
    }

    private fun observeMessageError() {
        mealPlanViewModel.messageError.observe(viewLifecycleOwner) { message: String? ->
                if (message != "") {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    mealPlanViewModel.resetMessageError()
                }
            }
    }

    private fun observeDeleteMelaPlanResult() {
        mealPlanViewModel.deleteMealPlanResult.observe(viewLifecycleOwner) { message ->
            if (message.contains("Success")) {
                mealPlanViewModel.getMealPlans(null)
            }
            if (message != "") {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMealPlans() {
        mealPlanViewModel.mealPlans.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mealplanGroup.visibility = View.VISIBLE
                        resource.data?.let {
                            val breakfastMealPlan = it.filter { item -> item.slot == 1 }
                            breakfastMealPlanAdapter.setMealPlans(breakfastMealPlan)
                            val lunchMealPlan = it.filter { item -> item.slot == 2 }
                            lunchMealPlanAdapter.setMealPlans(lunchMealPlan)
                            val dinnerMealPlan = it.filter { item -> item.slot == 3 }
                            dinnerMealPlanAdapter.setMealPlans(dinnerMealPlan)
                        }
                    }

                    is Resource.Error -> {
                        breakfastMealPlanAdapter.setMealPlans(ArrayList())
                        lunchMealPlanAdapter.setMealPlans(ArrayList())
                        dinnerMealPlanAdapter.setMealPlans(ArrayList())
                        binding.progressBar.visibility = View.GONE
                        binding.mealplanGroup.visibility = View.VISIBLE
                    }

                    is Resource.Loading -> {
                        binding.mealplanGroup.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}