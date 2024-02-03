package com.example.mealrecipeapp.ui.recipeinformation

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.R
import com.example.mealrecipeapp.databinding.FragmentRecipeInformationBinding
import com.example.mealrecipeapp.utils.Resource

class RecipeInformationFragment : Fragment() {
    private var _binding: FragmentRecipeInformationBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeInformationFragmentArgs by navArgs()

    private lateinit var recipeInformationViewModel: RecipeInformationViewModel
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var instructionAdapter: InstructionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        recipeInformationViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[RecipeInformationViewModel::class.java]

        val id = args.id
        recipeInformationViewModel.getRecipeInformation(id)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()
        observerRecipeInformation()
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter()
        binding.ingredientRecyclerView.adapter = ingredientAdapter
        instructionAdapter = InstructionAdapter()
        binding.instructionRecyclerView.adapter = instructionAdapter
    }

    private fun observerRecipeInformation() {
        recipeInformationViewModel.recipeInformation.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.informationGroup.visibility = View.VISIBLE
                    resource.data?.let {recipeInformation ->
                        ingredientAdapter.setIngredients(recipeInformation.extendedIngredients)
                        instructionAdapter.setInstructions(recipeInformation.analyzedInstructions[0].steps)
                        Glide.with(requireContext())
                            .load(recipeInformation.image)
                            .into(binding.posterImageView)
                        binding.titleTextView.text = recipeInformation.title
                        binding.likeTextView.text = resources.getString(R.string.like_counts, recipeInformation.aggregateLikes.toString())
                        binding.timeTextView.text = resources.getString(R.string.time_counts, recipeInformation.readyInMinutes.toString())
                        binding.servingTextView.text = resources.getString(R.string.serving_counts, recipeInformation.servings.toString())
                        binding.descriptionTextView.text = Html.fromHtml(
                            recipeInformation.summary,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        if (recipeInformation.isFavorite) {
                            binding.favoriteButton.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_favorite_filled
                            )
                        } else {
                            binding.favoriteButton.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_favorite
                            )
                        }
                        binding.favoriteButton.setOnClickListener {
                            if (recipeInformation.isFavorite) {
                                recipeInformationViewModel.removeFavoriteRecipe()
                            } else {
                                recipeInformationViewModel.addFavoriteRecipe()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.informationGroup.visibility = View.GONE
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.informationGroup.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}