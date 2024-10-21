plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Enables KAPT
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services") // Google services Gradle plugin
}

android {
    namespace = "com.example.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todoapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // General dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.play.services.tasks)
    testImplementation(libs.junit.junit)

    // Room dependencies
    val roomVersion = "2.6.1"
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:$roomVersion") // For Room annotation processing
    testImplementation(libs.androidx.room.testing) // For Room testing helpers

    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Coroutine dependencies
    implementation(libs.kotlinx.coroutines.android)

    // Navigation dependencies
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    // WorkManager dependencies
    implementation(libs.androidx.work.runtime.ktx)

    // Test dependencies
    testImplementation(libs.junit) // JUnit 4
    testImplementation(libs.mockito.core) // Mockito core
    testImplementation(libs.mockito.kotlin) // Mockito-Kotlin integration
    testImplementation(libs.kotlinx.coroutines.test) // Coroutine testing
    testImplementation(libs.androidx.core.testing) // For LiveData testing
    testImplementation (libs.mockito.inline)



    // Android instrumentation test dependencies
    androidTestImplementation(libs.androidx.junit) // Android JUnit
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    androidTestImplementation(libs.hilt.android.testing) // Hilt testing
    kaptAndroidTest(libs.hilt.android.compiler) // KAPT for Hilt in tests
}

kapt {
    correctErrorTypes = true
}
