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
import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.di.AppContainer;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        profileViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(ProfileViewModel.class);

        requireActivity().findViewById(R.id.button_sign_out).setOnClickListener(onClick -> {
            profileViewModel.signOut();
            NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_signInFragment);
        });
    }
}