plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.mealrecipeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mealrecipeapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation
    val navVersion = "2.7.6"
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Google Sign In
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    implementation("androidx.credentials:credentials:1.2.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")
}