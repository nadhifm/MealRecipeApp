package com.example.mealrecipeapp.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentSignInBinding
import com.example.mealrecipeapp.ui.dialog.LoadingDialog
import com.example.mealrecipeapp.utils.Resource
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var signInViewModel: SignInViewModel

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        signInViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[SignInViewModel::class.java]

        val credentialManager: CredentialManager = CredentialManager.create(requireContext())
        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
            .Builder("347653735490-n45emv6v7jdvat65jc48gruihov3ct37.apps.googleusercontent.com")
            .build()
        val request: GetCredentialRequest = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()

        binding.signInButton.iconTint = null
        binding.signInButton.setOnClickListener {

            ioScope.launch {
                val result = credentialManager.getCredential(
                    requireContext(),
                    request
                )
                val credentialData = result.credential.data
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credentialData)
                val email = googleIdTokenCredential.id
                val name = googleIdTokenCredential.displayName ?: ""
                var image = ""
                if (googleIdTokenCredential.profilePictureUri != null) {
                    image = googleIdTokenCredential.profilePictureUri.toString()
                }
                signInViewModel.saveUser(email, name, image)
            }
        }
        observeSignInResult()
    }

    private fun observeSignInResult() {
        val loadingDialog = LoadingDialog(requireContext())
        signInViewModel.signInResult.observe(viewLifecycleOwner) { resource->
            when (resource) {
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> loadingDialog.show()
            }
        }
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
        _binding = null
    }
}