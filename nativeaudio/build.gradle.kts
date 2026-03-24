
plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

android {
    namespace = "org.albaspazio.psysuite.nativeaudio"

    compileSdk = Configs.compileSdkVersion

    // Add this block
    lint {
        targetSdk = Configs.targetSdkVersion
    }

    defaultConfig {
        minSdk = Configs.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags("")
                arguments("-DANDROID_STL=c++_shared", "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON") // 16Kb compatibility
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(ProGuards.proguardTxt), ProGuards.androidDefault)        }
    }

    buildFeatures {
        prefab = true
    }

    externalNativeBuild {
        cmake {
           path("src/main/cpp/CMakeLists.txt")
            version = "3.31.6"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Configure native library alignment
    ndkVersion = "28.0.13004108"
}

dependencies {
    implementation("com.google.oboe:oboe:1.10.0")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
