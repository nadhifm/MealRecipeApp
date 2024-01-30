package com.example.mealrecipeapp.ui.favorite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.databinding.FragmentFavoriteBinding;
import com.example.mealrecipeapp.di.AppContainer;
import com.example.mealrecipeapp.ui.home.HomeViewModel;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    private FavoriteViewModel favoriteViewModel;

    private FavoriteRecipeAdapter favoriteRecipeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        favoriteViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(FavoriteViewModel.class);

        setupRecyclerView();
        observeFavoriteRecipe();
    }

    private void setupRecyclerView() {
        favoriteRecipeAdapter = new FavoriteRecipeAdapter(recipe -> {
            NavHostFragment.findNavController(this).navigate(FavoriteFragmentDirections.actionFavoriteFragmentToRecipeInformationFragment(recipe.getId()));
        });
        binding.favoriteRecyclerView.setAdapter(favoriteRecipeAdapter);
    }

    private void observeFavoriteRecipe() {
        favoriteViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), favoriteRecipe -> {
            favoriteRecipeAdapter.setRecipes(favoriteRecipe);
        });
    }
}