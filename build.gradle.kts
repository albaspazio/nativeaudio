// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.androidLibrary) version Versions.gradlePlugin apply(false)
    id(Plugins.kotlinAndroid) version Versions.kotlin apply(false)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}
