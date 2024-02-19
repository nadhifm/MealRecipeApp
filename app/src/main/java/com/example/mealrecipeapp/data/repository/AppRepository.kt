package com.example.mealrecipeapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.LiveData
import com.example.mealrecipeapp.data.local.database.RecipeDao
import com.example.mealrecipeapp.data.local.entity.RecipeEntity
import com.example.mealrecipeapp.data.remote.network.ApiService
import com.example.mealrecipeapp.data.remote.response.MealPlan
import com.example.mealrecipeapp.data.remote.response.MealPlanValue
import com.example.mealrecipeapp.data.remote.response.Recipe
import com.example.mealrecipeapp.data.remote.response.RecipeInformation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.scottyab.rootbeer.RootBeer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val auth: FirebaseAuth,
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

    private fun checkEmulatorProps(): Boolean {
        val props = listOf(
            "init.svc.qemud",
            "init.svc.qemu-props",
            "qemu.hw.mainkeys",
            "qemu.sf.fake_camera",
            "qemu.sf.lcd_density"
        )
        return props.any { propertyExists(it) }
    }

    private fun propertyExists(propertyName: String): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("getprop $propertyName")
            process.inputStream.bufferedReader().readText().isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    fun checkIsEmulator(): Boolean {
        val isEmulator = checkEmulatorProps()
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

    fun checkIsAccessibilityEnable(): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val isAccessibilityEnabled = am.isEnabled
        if (isAccessibilityEnabled) {
            crashlytics.recordException(Exception("Accessibility Service Is Enable"))
        }
        return isAccessibilityEnabled
    }

    fun getCheckAccessibilitySetting(): Boolean {
        return sharedPreferences.getBoolean("checkAccessibility", true)
    }

    fun setCheckAccessibilitySetting(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("checkAccessibility", isChecked)
        editor.apply()
    }

    suspend fun saveUser(): String {
        val currentUser = auth.currentUser

        if (currentUser != null && currentUser.email != null) {
            val editor = sharedPreferences.edit()
            val docRef = firestoreDB.collection("users").document(
                currentUser.email ?: ""
            )
            // check data in firestore
            docRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        editor.putString("username", document.getString("username"))
                        editor.putString("hash", document.getString("hash"))
                        editor.apply()
                    } else {
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
                                            editor.putString("username", it.username)
                                            editor.putString("hash", it.hash)
                                            editor.apply()
                                        }
                                        .addOnFailureListener {
                                            throw Exception("Fail Sign In")
                                        }
                                }
                            } else {
                                throw Exception("Fail Sign In")
                            }
                        }
                    }
                } else {
                    throw Exception("Fail Sign In")
                }
            }
        } else {
            throw Exception("Fail Sign In")
        }

        return "Success Sign In"
    }

    suspend fun deleteUser() {
        auth.signOut()
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("hash")
        editor.apply()
        recipeDao.deleteAll()
    }

    fun getUserEmail(): String {
        auth.currentUser?.let {
            return it.email ?: "Email"
        } ?: kotlin.run {
            return "Email"
        }
    }

    fun getUserName(): String {
        auth.currentUser?.let {
            return it.displayName ?: "Name"
        } ?: kotlin.run {
            return "Name"
        }
    }

    fun getUserImage(): String {
        auth.currentUser?.let {
            return it.photoUrl.toString()
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
