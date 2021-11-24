plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
    }
}

dependencies {

    // Hilt
    Dependencies.hilt.apply {
        implementation(mainHilt)
        kapt(compileAndroid)
    }

    // ROOM - dependency order is important
    Dependencies.room.apply {
        api(runtime)
        api(ktx)
        annotationProcessor(annotation)
        kapt(compiler)
    }

    // Coroutines
    Dependencies.coroutines.apply {
        api(coreCoroutines)
        api(androidCoroutines)
    }
}