plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
 //   id("org.jetbrains.kotlin.plugin.compose") version "2.0.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" // ✅ Add this for Kotlin 2.x + Compose
    id("kotlin-kapt") // Room ke liye
    id("com.google.gms.google-services") // Firebase ke liye
    id ("com.google.dagger.hilt.android")
    kotlin("kapt") // annotation processing ke liye
}

android {
    namespace = "com.example.demo.complaintApp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.demo.complaintApp"
        minSdk = 23 // Compose ke liye safe
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

// location
    implementation("com.google.android.gms:play-services-location:21.0.1")

// Firebase Storage
    implementation("com.google.firebase:firebase-storage")
    //google map
   // implementation("com.google.maps.android:maps-compose:4.3.0")
  //  implementation("com.google.android.gms:play-services-maps:19.0.0")
    // Camera
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.camera:camera-extensions:1.3.4")

    // Compose BOM – Master version control for Compose
    implementation(platform(libs.androidx.compose.bom))

    // Core Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Foundation (Box, Row, Column, LazyColumn etc.)
    implementation("androidx.compose.foundation:foundation")

    // Material3 (auto-version from BOM)
    implementation("androidx.compose.material3:material3")

    // Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Navigation
    implementation(libs.androidx.navigation.runtime.android)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.firestore)

    // Core libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Paging
    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")
    implementation("androidx.paging:paging-compose:3.3.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    testImplementation("androidx.room:room-testing:2.6.0")

    // Testing
    testImplementation ("io.mockk:mockk:1.13.10")
    androidTestImplementation ("io.mockk:mockk-android:1.13.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}


configurations.all {
    exclude(group = "org.junit.jupiter")
    exclude(group = "org.junit.platform")
}//Kisi bhi dependency ke through agar JUnit 5 aaye, use project me include mat karo