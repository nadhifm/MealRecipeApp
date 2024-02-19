package com.example.mealrecipeapp.ui.signin

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentSignInBinding
import com.example.mealrecipeapp.ui.dialog.LoadingDialog
import com.example.mealrecipeapp.utils.Constants
import com.example.mealrecipeapp.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var signInViewModel: SignInViewModel

    private val auth = FirebaseAuth.getInstance()

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

        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(Constants.GOOGLE_CLIENT_ID)
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInGoogleIntent = googleSignInClient.signInIntent

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(requireActivity()) { credentialTask ->
                            if (credentialTask.isSuccessful) {
                                signInViewModel.saveUser()
                            } else {
                                Toast.makeText(requireContext(), "Error: Authentication Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), "Error : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            googleSignInClient.signOut()
        }

        binding.signInButton.iconTint = null
        binding.signInButton.setOnClickListener {
            launcher.launch(signInGoogleIntent)
        }

        binding.signInGithubButton.iconTint = null
        binding.signInGithubButton.setOnClickListener {
            signInWithGithub()
        }

        observeSignInResult()
    }

    private fun signInWithGithub() {
        val provider = OAuthProvider.newBuilder("github.com")
        provider.scopes = listOf("user:email")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    signInViewModel.saveUser()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error : $it", Toast.LENGTH_LONG).show()
                }
        } else {
            auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener {
                    signInViewModel.saveUser()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error : $it", Toast.LENGTH_LONG).show()
                }
        }
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
        super.onDestroyView()
        _binding = null
    }
}