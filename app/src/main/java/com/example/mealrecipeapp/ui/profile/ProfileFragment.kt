package com.example.mealrecipeapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        profileViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[ProfileViewModel::class.java]

        binding.userNameTextView.text = profileViewModel.getUserName()
        binding.userEmailTextView.text = profileViewModel.getUserEmail()
        Glide.with(requireContext())
            .load(profileViewModel.getUserImage())
            .into(binding.userImageView)

        binding.signOutButton.setOnClickListener {
            profileViewModel.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
        }

        setupSwitch()
    }

    private fun setupSwitch() {
        binding.rootSwitch.isChecked = profileViewModel.getCheckRootSetting()
        binding.rootSwitch.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.setCheckRootSetting(isChecked)
        }

        binding.emulatorSwitch.isChecked = profileViewModel.getCheckEmulatorSetting()
        binding.emulatorSwitch.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.setCheckEmulatorSetting(isChecked)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}