package com.example.mealrecipeapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.example.mealrecipeapp.R;
import com.example.mealrecipeapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        navController.addOnDestinationChangedListener((controller, navDestination, bundle) -> {
            int navId = navDestination.getId();
            if (navId== R.id.homeFragment || navId == R.id.mealPlanFragment || navId == R.id.favoriteFragment || navId == R.id.profileFragment) {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            } else {
                binding.bottomNavigation.setVisibility(View.GONE);
            }
        });
    }
}