plugins {
    alias(libs.android.application) apply false
    alias(libs.android.library) apply false
    alias(libs.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.serializable) apply false
}

//
//buildscript {
//    dependencies {
//        classpath libs.google.services
//        classpath libs.hilt.android.gradle.plugin
//        classpath libs.kotlin.gradle.plugin
//    }
//    repositories {
//        mavenCentral()
//    }
//}// Top-level build file where you can add configuration options common to all sub-projects/modules.
