object Configs {

    const val compileSdkVersion = 29
    const val minSdkVersion     = 23
    const val targetSdkVersion  = 26
    const val versionCode       = 1
    const val versionName       = "1.0.0"
}

object Versions {
    const val kotlin = "1.4.10"
    const val gradlePlugin = "4.1.1"
}

object ClassPaths {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Plugins {
    const val androidLibrary        = "com.android.library"
    const val kotlinAndroid         = "android"
}

object ProGuards {
    val androidDefault = "proguard-rules.pro"
    val proguardTxt = "proguard-android.txt"
}