package com.example.mealrecipeapp.ui.recipeinformation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.data.remote.response.RecipeInformation;
import com.example.mealrecipeapp.databinding.FragmentRecipeInformationBinding;
import com.example.mealrecipeapp.di.AppContainer;
import com.example.mealrecipeapp.ui.home.HomeViewModel;

public class RecipeInformationFragment extends Fragment {

    private FragmentRecipeInformationBinding binding;
    private RecipeInformationViewModel recipeInformationViewModel;

    private IngredientAdapter ingredientAdapter;

    private InstructionAdapter instructionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        recipeInformationViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(RecipeInformationViewModel.class);

        Long id = RecipeInformationFragmentArgs.fromBundle(getArguments()).getId();
        recipeInformationViewModel.getRecipeInformation(id);

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        setupRecyclerView();
        observerRecipeInformation();
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter();
        binding.ingredientRecyclerView.setAdapter(ingredientAdapter);

        instructionAdapter = new InstructionAdapter();
        binding.instructionRecyclerView.setAdapter(instructionAdapter);
    }

    private void observerRecipeInformation() {
        recipeInformationViewModel.getRecipeInformationLiveData().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.informationGroup.setVisibility(View.VISIBLE);

                    RecipeInformation recipeInformation = resource.getData();

                    ingredientAdapter.setIngredients(recipeInformation.getExtendedIngredients());
                    instructionAdapter.setInstructions(recipeInformation.getAnalyzedInstructions().get(0).getSteps());

                    Glide.with(requireContext())
                            .load(recipeInformation.getImage())
                            .into(binding.posterImageView);

                    binding.titleTextView.setText(recipeInformation.getTitle());
                    binding.likeTextView.setText(recipeInformation.getAggregateLikes() + " likes");
                    binding.timeTextView.setText(recipeInformation.getReadyInMinutes() + " min");
                    binding.servingTextView.setText(recipeInformation.getServings() + " servings");
                    binding.descriptionTextView.setText(Html.fromHtml(recipeInformation.getSummary(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.informationGroup.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.informationGroup.setVisibility(View.GONE);
                    break;
            }
        });
    }
}