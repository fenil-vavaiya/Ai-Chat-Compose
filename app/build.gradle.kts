plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.google.services)

    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"

    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.ai_chat_compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ai_chat_compose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Jetpack Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // navigation animation
    implementation(libs.accompanist.navigation.animation)

    // firebase-auth
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)

    // Firebase BoM (always use the latest version)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Hilt Dependencies
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Hilt with Navigation (For Fragments)
    implementation(libs.androidx.hilt.navigation.fragment)

    // Hilt ViewModel
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // Hilt for Navigation Component (if needed)
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")


}