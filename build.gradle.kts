// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Android.kotlinVersion}")

        // HILT
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Android.hiltVersion}")

        // Safe Args
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Android.navigation}")

        // Serialization
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Android.serialization}")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}