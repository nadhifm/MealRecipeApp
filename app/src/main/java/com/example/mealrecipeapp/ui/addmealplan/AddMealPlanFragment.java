package com.example.mealrecipeapp.ui.addmealplan;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealrecipeapp.MealRecipeApp;
import com.example.mealrecipeapp.databinding.FragmentAddMealPlanBinding;
import com.example.mealrecipeapp.di.AppContainer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMealPlanFragment extends Fragment {

    private FragmentAddMealPlanBinding binding;
    private AddMealPlanViewModel addMealPlanViewModel;
    private AddMealPlanAdapter addMealPlanAdapter;
    private ActivityResultLauncher<PickVisualMediaRequest> launcherGallery;
    private ActivityResultLauncher<Uri> launcherCamera;
    private Uri currentUri = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMealPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContainer appContainer = ((MealRecipeApp) requireActivity().getApplication()).appContainer;
        addMealPlanViewModel = new ViewModelProvider(this, appContainer.viewModelFactory).get(AddMealPlanViewModel.class);

        binding.addImageButton.setOnClickListener(onClick -> new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Search Recipe By Image")
                .setMessage("Choose Your Media ?")
                .setNeutralButton("Cancel", (dialog, i) -> dialog.dismiss())
                .setPositiveButton("Camera", (dialog, i) -> startCamera())
                .setNegativeButton("Gallery", (dialog, i) -> startGallery()).show());

        setupIntent();
        setupRecyclerView();
        handleEditText();
        observeAddMealPlanResult();
        observeRecipes();
    }

    private void setupRecyclerView() {
        addMealPlanAdapter = new AddMealPlanAdapter(recipe -> {
            int slot = AddMealPlanFragmentArgs.fromBundle(getArguments()).getSlot();
            Long date = AddMealPlanFragmentArgs.fromBundle(getArguments()).getDate();
            addMealPlanViewModel.addMealPlan(date, slot, recipe);
        });
        binding.recipeRecyclerView.setAdapter(addMealPlanAdapter);
    }

    private void setupIntent() {
        launcherGallery = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                currentUri = uri;
                getImageLabel();
            } else {

            }
        });

        launcherCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess  -> {
            if (isSuccess) {
                if (currentUri != null) {
                    getImageLabel();
                } else {

                }
            }
        });
    }

    private void handleEditText() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addMealPlanViewModel.searchRecipes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void observeAddMealPlanResult() {
        addMealPlanViewModel.getAddMealPlanResult().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void observeRecipes() {
        addMealPlanViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recipeRecyclerView.setVisibility(View.VISIBLE);
                    addMealPlanAdapter.setRecipes(resource.getData());
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recipeRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    binding.recipeRecyclerView.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private void getImageLabel() {
        try {
            InputImage image = InputImage.fromFilePath(requireContext(), currentUri);
            ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
            labeler.process(image)
                    .addOnSuccessListener(labels -> binding.searchEditText.setText(labels.get(0).getText()))
                    .addOnFailureListener(e -> {

                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Uri getImageUri(Context context) {
        final String fileNameFormat  = "yyyyMMdd_HHmmss";
        final String timestamp = new SimpleDateFormat(fileNameFormat, Locale.US).format(new Date());
        Uri uri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/");

            uri = context.getContentResolver().insert(

                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
            );
        }

        if (uri == null) {
            return getImageUriForPreQ(context);
        } else {
            return uri;
        }
    }

    private Uri getImageUriForPreQ(Context context) {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(filesDir, "/MyCamera/$timeStamp.jpg");
        if (imageFile.getParentFile() != null) {
            if (!imageFile.getParentFile().exists()) {
                imageFile.getParentFile().mkdir();
            }
        }
        return FileProvider.getUriForFile(
                context,
                "com.example.mealrecipeapp.fileprovider",
                imageFile
        );
    }
    private void startCamera() {
        currentUri = getImageUri(requireContext());
        launcherCamera.launch(currentUri);
    }
    private void startGallery() {
        launcherGallery.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}