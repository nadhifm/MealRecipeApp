package com.example.mealrecipeapp.ui.addmealplan

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.mealrecipeapp.MealRecipeApp
import com.example.mealrecipeapp.databinding.FragmentAddMealPlanBinding
import com.example.mealrecipeapp.ui.dialog.LoadingDialog
import com.example.mealrecipeapp.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMealPlanFragment : Fragment() {
    private var _binding: FragmentAddMealPlanBinding? = null
    private val binding get() = _binding!!

    private val args: AddMealPlanFragmentArgs by navArgs()

    private lateinit var addMealPlanViewModel: AddMealPlanViewModel
    private lateinit var addMealPlanAdapter: AddMealPlanAdapter

    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var launcherCamera: ActivityResultLauncher<Uri?>
    private var currentUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (requireActivity().application as MealRecipeApp).appContainer
        addMealPlanViewModel = ViewModelProvider(this, appContainer.viewModelFactory)[AddMealPlanViewModel::class.java]

        binding.addImageButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Search Recipe By Image")
                .setMessage("Choose Your Media ?")
                .setNeutralButton("Cancel") { dialog: DialogInterface, _ -> dialog.dismiss() }
                .setPositiveButton("Camera") { _, _ -> startCamera() }
                .setNegativeButton("Gallery") { _, _ -> startGallery() }
                .show()
        }
        binding.backButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }

        setupIntent()
        setupRecyclerView()
        handleEditText()
        observeAddMealPlanResult()
        observeRecipes()
    }

    private fun setupRecyclerView() {
        addMealPlanAdapter = AddMealPlanAdapter { recipe ->
            val slot = args.slot
            val date = args.date
            addMealPlanViewModel.addMealPlan(date, slot, recipe)
        }
        binding.recipeRecyclerView.adapter = addMealPlanAdapter
    }

    private fun setupIntent() {
        launcherGallery =
            registerForActivityResult<PickVisualMediaRequest, Uri>(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    currentUri = uri
                    imageLabel()
                } else {
                    Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            }
        launcherCamera =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    if (currentUri != null) {
                        imageLabel()
                    } else {
                        Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun handleEditText() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                addMealPlanViewModel.setQuery(charSequence.toString())
                addMealPlanAdapter.setRecipes(ArrayList())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun observeAddMealPlanResult() {
        val loadingDialog = LoadingDialog(requireContext())

        addMealPlanViewModel.addMealPlanResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    Toast.makeText(activity, resource.data, Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> loadingDialog.show()
            }
        }
    }

    private fun observeRecipes() {
        addMealPlanViewModel.recipes.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recipeRecyclerView.visibility = View.VISIBLE
                        resource.data?.let {
                            addMealPlanAdapter.setRecipes(resource.data)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recipeRecyclerView.visibility = View.GONE
                        Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {
                        binding.recipeRecyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
    }

    private fun imageLabel() {
        currentUri?.let {
            try {
                val image = InputImage.fromFilePath(requireContext(), it)
                val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                labeler.process(image)
                    .addOnSuccessListener { labels: List<ImageLabel> ->
                        binding.searchEditText.setText(
                            labels[0].text
                        )
                    }
                    .addOnFailureListener { e: Exception? -> }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun getImageUri(context: Context): Uri {
        val fileNameFormat = "yyyyMMdd_HHmmss"
        val timestamp = SimpleDateFormat(fileNameFormat, Locale.getDefault()).format(Date())
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$timestamp.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        }
        return uri ?: getImageUriForPreQ(context)
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/\$timeStamp.jpg")
        imageFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdir()
            }
        }
        return FileProvider.getUriForFile(
            context,
            "com.example.mealrecipeapp.fileprovider",
            imageFile
        )
    }

    private fun startCamera() {
        currentUri = getImageUri(requireContext())
        launcherCamera.launch(currentUri)
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest.Builder()
                .setMediaType(ImageOnly)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}