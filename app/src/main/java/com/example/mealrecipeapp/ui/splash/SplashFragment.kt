package com.example.mealrecipeapp.ui.splash

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

        checkSystem()
        checkUserEmail()
    }

    private fun checkSystem() {
        var isRooted = false
        var isEmulator = false
        var isUSBDebugEnable = false
        var isAccessibilityEnable = false

        if (splashViewModel.getCheckRootSetting()) {
            isRooted = splashViewModel.checkIsRooted()
        }

        if (splashViewModel.getCheckEmulatorSetting()) {
            isEmulator = splashViewModel.checkIsEmulator()
        }

        if (splashViewModel.getCheckUSBDebugSetting()) {
            isUSBDebugEnable = splashViewModel.checkIsUSBDebugEnable()
        }

        if (splashViewModel.getCheckAccessibilitySetting()) {
            isAccessibilityEnable = splashViewModel.checkIsAccessibilityEnable()
        }

        var message = ""

        if (isRooted) {
            message += "\n - Your Device Is Rooted"
        }
        if (isEmulator) {
            message += "\n - Your Device Is Emulator"
        }
        if (isUSBDebugEnable) {
            message += "\n - USB Debugging Is Enable"
        }
        if (isAccessibilityEnable) {
            message += "\n - Accessibility Service Is Enable"
        }

        if (message != "") {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Information")
                .setMessage(message + "\n")
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