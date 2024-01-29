package com.example.mealrecipeapp.ui.profile;

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
import com.example.mealrecipeapp.databinding.FragmentProfileBinding;
import com.example.mealrecipeapp.di.AppContainer;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        profileViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(ProfileViewModel.class);

        binding.buttonSignOut.setOnClickListener(onClick -> {
            profileViewModel.signOut();
            NavHostFragment.findNavController(this).navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment());
        });
    }
}