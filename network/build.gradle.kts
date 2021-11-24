plugins {
    id ("com.android.library")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk

        buildConfigField ("String", "MY_URL", "\"https://newsapi.org\"")
        buildConfigField("String", "MY_KEY", "\"8faf141d20ff46de82ad1c4a76a0dbbe\"")
    }
}

dependencies {

    // Hilt
    Dependencies.hilt.apply {
        implementation(mainHilt)
        kapt(compileAndroid)
    }

    // Retrofit
    Dependencies.retrofit.apply {
        api (retrofit)
        api (gson)
        api (loggingInterceptor)
    }
}