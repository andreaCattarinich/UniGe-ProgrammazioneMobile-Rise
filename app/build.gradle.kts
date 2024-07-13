plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Firebase
    id("com.google.gms.google-services")

    // Safe Args
    id("androidx.navigation.safeargs.kotlin")

    // Databinding
    id("kotlin-kapt")
}

android {
    namespace = "com.unige.rise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unige.rise"
        minSdk = 30
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")

    // Firebase
    //implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-firestore")

    // Firebase authentication
    //implementation("com.google.firebase:firebase-auth")
    //implementation("com.google.firebase:firebase-auth:23.0.0")
    //implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    // if you target Android 12
    //implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Firebase Cloud Storage
    //implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-storage:21.0.0")

    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Firebase Cloud Messaging (FCM)
    //implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-messaging:24.0.0")


    /*** Firebase Authentication Android Google ***/
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")


}