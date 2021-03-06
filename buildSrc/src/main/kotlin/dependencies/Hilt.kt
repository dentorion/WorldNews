@file:Suppress("unused")

package dependencies

object Hilt {

    const val mainHilt = "com.google.dagger:hilt-android:${Versions.mainHilt}"

    const val compile = "com.google.dagger:hilt-compiler:${Versions.compile}"
    const val compileAndroid = "com.google.dagger:hilt-android-compiler:${Versions.compileAndroid}"
}