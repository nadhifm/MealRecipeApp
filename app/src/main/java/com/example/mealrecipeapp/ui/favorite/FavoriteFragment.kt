package com.example.mealrecipeapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteRecipeAdapter: FavoriteRecipeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        favoriteViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[FavoriteViewModel::class.java]
        setupRecyclerView()
        observeFavoriteRecipe()
    }

    private fun setupRecyclerView() {
        favoriteRecipeAdapter = FavoriteRecipeAdapter { recipeId ->
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToRecipeInformationFragment(recipeId)
            )
        }
        binding.favoriteRecyclerView.adapter = favoriteRecipeAdapter
    }

    private fun observeFavoriteRecipe() {
        favoriteViewModel.favoriteRecipes.observe(viewLifecycleOwner) { favoriteRecipe ->
                favoriteRecipeAdapter.setRecipes(favoriteRecipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}