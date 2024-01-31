package com.example.mealrecipeapp.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.databinding.FragmentSignInBinding;
import com.example.mealrecipeapp.di.AppContainer;
import com.example.mealrecipeapp.ui.dialog.LoadinDialog;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    private SignInViewModel signInViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        signInViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(SignInViewModel.class);

        CredentialManager credentialManager = CredentialManager.create(requireContext());
        GetSignInWithGoogleOption googleIdOption = new GetSignInWithGoogleOption
                .Builder("347653735490-n45emv6v7jdvat65jc48gruihov3ct37.apps.googleusercontent.com")
                .build();
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        binding.signInButton.setIconTint(null);

        binding.signInButton.setOnClickListener(onClick -> credentialManager.getCredentialAsync(
            requireActivity(),
            request,
            null,
            requireActivity().getMainExecutor(),
            new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                @Override
                public void onResult(GetCredentialResponse getCredentialResponse) {
                    Bundle credentialData = getCredentialResponse.getCredential().getData();

                    GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                    String email = googleIdTokenCredential.getId();
                    String name = googleIdTokenCredential.getDisplayName();
                    String image = "";
                    if (googleIdTokenCredential.getProfilePictureUri() != null) {
                        image = googleIdTokenCredential.getProfilePictureUri().toString();
                    }

                    signInViewModel.saveUser(email, name, image);
                }

                @Override
                public void onError(@NonNull GetCredentialException e) {

                }
            }
        ));

        observeSignInResult();
    }

    private void observeSignInResult() {
        LoadinDialog loadinDialog = new LoadinDialog(requireContext());
        signInViewModel.getSignInResult().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    loadinDialog.dismiss();
                    NavHostFragment.findNavController(this).navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment());
                    break;
                case ERROR:
                    loadinDialog.dismiss();
                    Toast.makeText(getActivity(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    loadinDialog.show();
                    break;
            }
        });
    }
}