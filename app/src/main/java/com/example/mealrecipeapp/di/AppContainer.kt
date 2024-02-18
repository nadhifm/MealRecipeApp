package com.example.mealrecipeapp.di

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mealrecipeapp.data.local.database.AppDatabase
import com.example.mealrecipeapp.data.remote.network.ApiService
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.ui.ViewModelFactory
import com.example.mealrecipeapp.utils.Constants
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.scottyab.rootbeer.RootBeer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(appContext: Context) {
    var viewModelFactory: ViewModelFactory
    init {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "UserPreferences",
            masterKey,
            appContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        val apiService = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
        val appDatabase = AppDatabase.getInstance(appContext)
        val firestoreDB = FirebaseFirestore.getInstance()
        val crashlytics = FirebaseCrashlytics.getInstance()
        val rootBeer = RootBeer(appContext)
        val appRepository = AppRepository(
            appContext,
            apiService,
            sharedPreferences,
            appDatabase.recipeDao(),
            firestoreDB,
            crashlytics,
            rootBeer,
        )
        viewModelFactory = ViewModelFactory(appRepository)
    }
}
