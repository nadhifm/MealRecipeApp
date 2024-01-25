package com.example.mealrecipeapp.ui.signin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;

import com.example.mealrecipeapp.R;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

public class SignInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView usernameText = requireActivity().findViewById(R.id.text_view_username);

        CredentialManager credentialManager = CredentialManager.create(requireContext());
        GetSignInWithGoogleOption googleIdOption = new GetSignInWithGoogleOption
                .Builder("347653735490-n45emv6v7jdvat65jc48gruihov3ct37.apps.googleusercontent.com")
                .build();
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        requireActivity().findViewById(R.id.button_sign_in).setOnClickListener(onClick -> {
            credentialManager.getCredentialAsync(
                requireActivity(),
                request,
                null,
                requireActivity().getMainExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse getCredentialResponse) {
                        Bundle credentialData = getCredentialResponse.getCredential().getData();

                        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                        usernameText.setText(googleIdTokenCredential.getId());
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("///", e.getLocalizedMessage());
                    }
                }
            );
        });
    }
}