package com.example.mealrecipeapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealrecipeapp.R;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private RecipeAdapter recipeAdapter;
    private RecyclerView recipeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView = requireActivity().findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setAdapter(recipeAdapter);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    // Data loaded successfully, hide loading indicator and update UI
                    requireActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    recipeAdapter.setRecipes(resource.getData());
                    break;
                case ERROR:
                    // Error loading data, hide loading indicator and show error message
                    requireActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    Toast.makeText(getActivity(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Data is still loading, show loading indicator
                    requireActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    break;
            }
        });
    }
}