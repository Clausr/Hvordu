plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles += "consumer-rules.pro"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            proguardFiles += getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "dk.clausr.core"
}

dependencies {
    api(libs.timber)
    implementation(libs.androidx.ktx)
    testImplementation(libs.junit)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.datetime)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}