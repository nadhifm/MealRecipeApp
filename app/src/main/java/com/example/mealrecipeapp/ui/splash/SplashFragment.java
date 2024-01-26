package com.example.mealrecipeapp.ui.splash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.di.AppContainer;
import com.example.mealrecipeapp.ui.signin.SignInViewModel;

public class SplashFragment extends Fragment {

    private SplashViewModel splashViewModel;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        splashViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(SplashViewModel.class);
        navController = NavHostFragment.findNavController(this);

        new Handler().postDelayed(() -> {
            if (splashViewModel.checkEmailUser().equals("Email")) {
                navController.navigate(R.id.action_splashFragment_to_signInFragment);
            } else {
                navController.navigate(R.id.action_splashFragment_to_homeFragment);
            }
        }, 2000);
    }
}