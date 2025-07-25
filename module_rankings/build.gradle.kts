plugins {
    //alias(libs.plugins.android.application)
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
    namespace = "com.example.module_rankings"
    compileSdk = 35

    defaultConfig {
//        applicationId = "com.example.module_rankings"
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
    kapt(libs.arouter.compiler)
    implementation(libs.arouter.api)
    implementation ("androidx.media3:media3-exoplayer:1.1.1")
    implementation ("androidx.media3:media3-ui:1.1.1")
    implementation ("androidx.media3:media3-common:1.1.1")
    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.14")

    implementation(project(":lib_base"))
    implementation(project(":lib_net"))
}