package com.example.mealrecipeapp.ui.splash

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentSplashBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var splashViewModel: SplashViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        splashViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[SplashViewModel::class.java]

        checkRootAndEmulator()
        checkUserEmail()
    }

    private fun checkRootAndEmulator() {
        var isRooted = false
        var isEmulator = false

        if (splashViewModel.getCheckRootSetting()) {
            if (splashViewModel.checkIsRooted()) {
                isRooted = true
            }
        }

        if (splashViewModel.getCheckEmulatorSetting()) {
            if (splashViewModel.checkIsEmulator()) {
                isEmulator = true
            }
        }

        val message = if (isRooted && isEmulator) {
            "Your Device Is Emulator and Rooted"
        } else if (isRooted) {
            "Your Device Is Rooted"
        } else if (isEmulator) {
            "Your Device Is Emulator"
        } else {
            ""
        }

        if (message != "") {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(message)
                .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun checkUserEmail() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (splashViewModel.checkUserEmail() == "Email") {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
            }
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}