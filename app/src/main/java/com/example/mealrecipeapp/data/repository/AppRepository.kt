package com.example.mealrecipeapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.lifecycle.LiveData
import com.example.mealrecipeapp.data.local.database.RecipeDao
import com.example.mealrecipeapp.data.local.entity.RecipeEntity
import com.example.mealrecipeapp.data.remote.network.ApiService
import com.example.mealrecipeapp.data.remote.response.MealPlan
import com.example.mealrecipeapp.data.remote.response.MealPlanValue
import com.example.mealrecipeapp.data.remote.response.Recipe
import com.example.mealrecipeapp.data.remote.response.RecipeInformation
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.scottyab.rootbeer.RootBeer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(
    private val context: Context,
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences,
    private val recipeDao: RecipeDao,
    private val firestoreDB: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics,
    private val rootBeer: RootBeer,
) {
    fun checkIsRooted(): Boolean {
        val isRooted =  rootBeer.isRooted
        if (isRooted) {
            crashlytics.recordException(Exception("Device Is Rooted"))
        }
        return isRooted
    }

    fun getCheckRootSetting(): Boolean {
        return sharedPreferences.getBoolean("checkRooted", true)
    }

    fun setCheckRootSetting(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("checkRooted", isChecked)
        editor.apply()
    }

    private val GENY_FILES = arrayOf(
        "/dev/socket/genyd",
        "/dev/socket/baseband_genyd"
    )
    private val PIPES = arrayOf(
        "/dev/socket/qemud",
        "/dev/qemu_pipe"
    )
    private val X86_FILES = arrayOf(
        "ueventd.android_x86.rc",
        "x86.prop",
        "ueventd.ttVM_x86.rc",
        "init.ttVM_x86.rc",
        "fstab.ttVM_x86",
        "fstab.vbox86",
        "init.vbox86.rc",
        "ueventd.vbox86.rc"
    )
    private val ANDY_FILES = arrayOf(
        "fstab.andy",
        "ueventd.andy.rc"
    )
    private val NOX_FILES = arrayOf(
        "fstab.nox",
        "init.nox.rc",
        "ueventd.nox.rc"
    )
    private fun checkFiles(targets: Array<String>): Boolean {
        for (pipe in targets) {
            val file = File(pipe)
            if (file.exists()) {
                return true
            }
        }
        return false
    }

    fun checkIsEmulator(): Boolean {
        val isEmulator = (checkFiles(GENY_FILES)
                || checkFiles(ANDY_FILES)
                || checkFiles(NOX_FILES)
                || checkFiles(X86_FILES)
                || checkFiles(PIPES))
        if (isEmulator) {
            crashlytics.recordException(Exception("Device Is Emulator"))
        }
        return isEmulator
    }

    fun getCheckEmulatorSetting(): Boolean {
        return sharedPreferences.getBoolean("checkEmulator", true)
    }

    fun setCheckEmulatorSetting(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("checkEmulator", isChecked)
        editor.apply()
    }
    fun checkIsUSBDebugEnable(): Boolean {
        val isUSBDebugEnable = Settings.Secure.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
        if (isUSBDebugEnable) {
            crashlytics.recordException(Exception("USB Debugging Is Enable"))
        }
        return isUSBDebugEnable
    }

    fun getCheckUSBDebugSetting(): Boolean {
        return sharedPreferences.getBoolean("checkUSBDebug", true)
    }

    fun setCheckUSBDebugSetting(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("checkUSBDebug", isChecked)
        editor.apply()
    }

    suspend fun saveUser(email: String, name: String, image: String): String {
        var resource = ""
        val editor = sharedPreferences.edit()
        val docRef = firestoreDB.collection("users").document(
            email
        )
        // check data in firestore
        docRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    // data exist in firestore
                    // save data user to shared preferences
                    editor.putString("email", email)
                    editor.putString("name", name)
                    editor.putString("image", image)
                    editor.putString("username", document.getString("username"))
                    editor.putString("hash", document.getString("hash"))
                    editor.apply()
                    resource = "Success Sign In"
                } else {
                    // data not exist in firestore
                    // create data user from api
                    CoroutineScope(Dispatchers.IO).launch {
                        val call = apiService.connectUser()
                        if (call.isSuccessful) {
                            call.body()?.let {
                                // success create data user from api
                                // save data user to firestore
                                val user: MutableMap<String, Any> = HashMap()
                                user["username"] = it.username
                                user["hash"] = it.hash
                                docRef.set(user)
                                    .addOnSuccessListener { _ ->
                                        // success save data user to firestore
                                        // save data user to shared preferences
                                        editor.putString("email", email)
                                        editor.putString("name", name)
                                        editor.putString("image", image)
                                        editor.putString("username", it.username)
                                        editor.putString("hash", it.hash)
                                        editor.apply()
                                        resource = "Success Sign In"
                                    }
                                    .addOnFailureListener {
                                        resource = "Fail Sign In"
                                    }
                            }
                        } else {
                            resource = "Fail Sign In"
                        }
                    }
                }
            } else {
                resource = "Fail Sign In"
            }
        }
        return resource
    }

    suspend fun deleteUser() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("name")
        editor.apply()
        recipeDao.deleteAll()
    }

    fun getUserEmail(): String {
        sharedPreferences.getString("email", "Email")?.let {
            return it
        } ?: kotlin.run {
            return "Email"
        }
    }

    fun getUserName(): String {
        sharedPreferences.getString("name", "Name")?.let {
            return it
        } ?: kotlin.run {
            return "Name"
        }
    }

    fun getUserImage(): String {
        sharedPreferences.getString("image", "")?.let {
            return it
        } ?: kotlin.run {
            return ""
        }
    }

    suspend fun getRecipes(query: String): List<Recipe> {
        val resource = mutableListOf<Recipe>()
        try {
            val call = apiService.getRecipes(25, "popularity", query)
            if (call.code() != 200 && call.body() == null) {
                throw Exception("Data Empty")
            } else {
                call.body()?.let {
                    val recipeList = it.results.filter { recipe -> recipe.analyzedInstructions.isNotEmpty() && recipe.extendedIngredients.isNotEmpty() }
                    resource.addAll(recipeList)
                }
            }
        } catch (e: Exception) {
            throw Exception("No Data")
        }

        return resource
    }

    suspend fun addMealPlan(date: Long, slot: Int, recipe: Recipe): String {
        val result: String
        val username = sharedPreferences.getString("username", "") ?: ""
        val hash = sharedPreferences.getString("hash", "") ?: ""
        val valueBody =
            MealPlanValue(recipe.id, recipe.servings, recipe.title, recipe.image)
        val body = MealPlan(0L, date / 1000 + 25200, slot, 0, "RECIPE", valueBody)
        val call = apiService.addMealPlan(username, hash, body)
        result = if (call.isSuccessful) {
            "Success Add Meal Plan"
        } else {
            "Fail Add Meal Plan"
        }

        return result
    }

    suspend fun getRecipeInformation(id: Long): RecipeInformation {
        var result = RecipeInformation(
            0,
            "",
            "",
            "",
            "",
            0,
            0,
            0,
            listOf(),
            listOf(),
            false
        )
        val call = apiService.getRecipeInformation(id)
        if (call.code() == 200) {
            call.body()?.let {
                val recipeInformation = it
                val favoriteCount = recipeDao.getFavoriteRecipeById(
                    recipeInformation.id
                )
                if (favoriteCount != 0) {
                    recipeInformation.isFavorite = true
                }
                result = recipeInformation
            }
        } else {
            throw Exception("Fail Get Recipe Information")
        }
        return result
    }

    suspend fun deleteMealPlan(id: Long): String {
        val result: String
        val username = sharedPreferences.getString("username", "") ?: ""
        val hash = sharedPreferences.getString("hash", "") ?: ""
        val call = apiService.deleteMealPlan(username, id, hash)
        result = if (call.isSuccessful) {
            "Success Delete Meal Plan"
        } else {
            "Fail Delete Meal Plan"
        }
        return result
    }

    suspend fun getMealPlans(date: Date): List<MealPlan> {
        val mealPlan = mutableListOf<MealPlan>()
        val username = sharedPreferences.getString("username", "") ?: ""
        val hash = sharedPreferences.getString("hash", "") ?: ""
        val dateFormat = "yyyy-MM-dd"
        val formattedDate = SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
        val call = apiService.getMealPlans(username, formattedDate, hash)
        if (call.code() == 200) {
            call.body()?.let {
                mealPlan.addAll(it.items)
            }
        } else {
            throw Exception("No meals planned for that day")
        }
        return mealPlan
    }

    suspend fun addFavoriteRecipe(recipeEntity: RecipeEntity) {
        recipeDao.insert(recipeEntity)
    }

    suspend fun removeFavoriteRecipe(recipeEntity: RecipeEntity) {
        recipeDao.delete(recipeEntity)
    }

    fun getFavoriteRecipes(): LiveData<List<RecipeEntity>> = recipeDao.favoriteRecipes()
}
