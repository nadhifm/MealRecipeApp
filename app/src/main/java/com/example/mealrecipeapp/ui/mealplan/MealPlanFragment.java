package com.example.mealrecipeapp.ui.mealplan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.databinding.FragmentMealPlanBinding;
import com.example.mealrecipeapp.di.AppContainer;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealPlanFragment extends Fragment {

    private FragmentMealPlanBinding binding;
    private MealPlanViewModel mealPlanViewModel;
    private MaterialDatePicker<Long> datePicker;
    private MealPlanAdapter breakfastMealPlanAdapter;
    private MealPlanAdapter lunchMealPlanAdapter;
    private MealPlanAdapter dinnerMealPlanAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mealPlanViewModel.getMealPlans(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        mealPlanViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(MealPlanViewModel.class);

        binding.dateTextView.setOnClickListener(v -> datePicker.show(requireActivity().getSupportFragmentManager(), "date-picker"));

        binding.leftButton.setOnClickListener(v -> mealPlanViewModel.setPrevDate());

        binding.rightButton.setOnClickListener(v -> mealPlanViewModel.setNextDate());

        setupRecyclerView();
        observeDate();
        observeDeleteMelaPlanResult();
        observeMessageError();
        observeMealPlans();
    }

    private void setupRecyclerView() {
        breakfastMealPlanAdapter = new MealPlanAdapter(
            mealPlan -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(mealPlan.getValue().getId())),
            mealPlan -> mealPlanViewModel.deleteMealPlan(mealPlan.getId())
        );
        binding.breakfastRecyclerView.setAdapter(breakfastMealPlanAdapter);

        lunchMealPlanAdapter = new MealPlanAdapter(
            mealPlan -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(mealPlan.getValue().getId())),
            mealPlan -> mealPlanViewModel.deleteMealPlan(mealPlan.getId())
        );
        binding.lunchRecyclerView.setAdapter(lunchMealPlanAdapter);

        dinnerMealPlanAdapter = new MealPlanAdapter(
            mealPlan -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToRecipeInformationFragment(mealPlan.getValue().getId())),
            mealPlan -> mealPlanViewModel.deleteMealPlan(mealPlan.getId())
        );
        binding.dinnerRecyclerView.setAdapter(dinnerMealPlanAdapter);
    }

    private void observeDate() {
        mealPlanViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            final String dateFormat  = "E, dd LLL yyyy";
            final String selectedDate = new SimpleDateFormat(dateFormat, Locale.getDefault()).format(date);

            binding.dateTextView.setText(selectedDate);

            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(date.getTime())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> mealPlanViewModel.setDate(new Date(selection)));

            binding.addBreakfastButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(1, date.getTime())));
            binding.addLunchButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(2, date.getTime())));
            binding.addDinnerButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(MealPlanFragmentDirections.actionMealPlanFragmentToAddMealPlanFragment(3, date.getTime())));
        });
    }

    private void observeMessageError() {
        mealPlanViewModel.getMessageErrorLiveData().observe(getViewLifecycleOwner(), message -> {
            if (!message.equals("")) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                mealPlanViewModel.resetMessageError();
            }
        });
    }

    private void observeDeleteMelaPlanResult() {
        mealPlanViewModel.getDeleteMealPlanResultLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message.contains("Success")) {
                mealPlanViewModel.getMealPlans(null);
            }
            if (!message.equals("")) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeMealPlans() {
        mealPlanViewModel.getMealPlansLiveData().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.mealplanGroup.setVisibility(View.VISIBLE);

                    List<MealPlan> breakfastMealPlan = new ArrayList<>(resource.getData());
                    breakfastMealPlan.removeIf(item -> item.getSlot() != 1);
                    breakfastMealPlanAdapter.setMealPlans(breakfastMealPlan);

                    List<MealPlan> lunchMealPlan = new ArrayList<>(resource.getData());
                    lunchMealPlan.removeIf(item -> item.getSlot() != 2);
                    lunchMealPlanAdapter.setMealPlans(lunchMealPlan);

                    List<MealPlan> dinnerMealPlan = new ArrayList<>(resource.getData());
                    dinnerMealPlan.removeIf(item -> item.getSlot() != 3);
                    dinnerMealPlanAdapter.setMealPlans(dinnerMealPlan);
                    break;
                case ERROR:
                    breakfastMealPlanAdapter.setMealPlans(new ArrayList<>());
                    lunchMealPlanAdapter.setMealPlans(new ArrayList<>());
                    dinnerMealPlanAdapter.setMealPlans(new ArrayList<>());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.mealplanGroup.setVisibility(View.VISIBLE);
                    break;
                case LOADING:
                    binding.mealplanGroup.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }
}