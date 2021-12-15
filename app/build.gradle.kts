plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        applicationId = "com.entin.worldnews"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {

    implementation(project(mapOf("path" to ":network")))
    implementation(project(mapOf("path" to ":db")))

    // Main dependencies of project
    Dependencies.base.apply {
        api(coreKtx)
        api(appcompat)
        api(material)
        api(constraintlayout)
        implementation(recyclerView)
        implementation(swipe)

        testImplementation(jUnit)
        androidTestImplementation(jUnitTest)
        androidTestImplementation(espresso)
    }

    // Lifecycle + ViewModel & LiveData
    Dependencies.lifecycle.apply {
        implementation(lifecycle)
        implementation(liveData)
        implementation(viewModel)
    }

    // Hilt
    Dependencies.hilt.apply {
        implementation(mainHilt)
        kapt(compileAndroid)
    }

    // Glide library
    Dependencies.glide.apply {
        implementation(mainGlide)
    }

    // NAVIGATION COMPONENT
    Dependencies.navigation.apply {
        implementation(mainNavigation)
        implementation(ui)
    }

    // WorkManager
    Dependencies.workManager.apply {
        implementation(workManager)
    }

    // ViewBinding delegate [With REFLECTION] - Kirill Rozov
    Dependencies.viewBindingDelegate.apply {
        implementation(main)
    }
}