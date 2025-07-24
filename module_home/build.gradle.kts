plugins {
    // alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.android.library")
    kotlin("kapt")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    namespace = "com.generals.module.home"
    compileSdk = 35

    defaultConfig {
//        applicationId = "com.generals.module.home"
        minSdk = 26
        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.bundles.projectBase)
    implementation(libs.arouter.api)
    kapt(libs.arouter.compiler)

    implementation(project(":lib_base"))
    implementation(project(":lib_net"))
    implementation(project(":module_discover"))
    implementation(project(":module_square"))
    implementation(project(":module_rankings"))
    implementation("androidx.core:core-splashscreen:1.0.1")

}