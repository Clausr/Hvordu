import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
}
val keystorePropertiesFile = rootProject.file("signing/secrets.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

fun getGitCommitCount(): Int {
    val process = Runtime.getRuntime().exec("git rev-list --count HEAD")
    return process.inputStream.bufferedReader().use { it.readText().trim().toInt() }
}


android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dk.clausr.hvordu"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = getGitCommitCount()
        versionName = "1.0"

        // Set value part
        val properties = Properties()

        val localPropertiesFile = rootProject.file("local.properties")
        properties.load(FileInputStream(localPropertiesFile))
        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            "\"${properties.getProperty("SUPABASE_ANON_KEY")}\""
        )
        buildConfigField("String", "SECRET", "\"${properties.getProperty("SECRET")}\"")
        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("SUPABASE_URL")}\"")
    }


    signingConfigs {
        create("release") {
            storeFile = rootProject.file("signing/Clausr.keystore")
            storePassword = keystoreProperties["SIGNING_STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["GOOGLE_PLAY_SIGNING_KEY_ALIAS"] as String
            keyPassword = keystoreProperties["SIGNING_KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    namespace = "dk.clausr.hvordu"
}

dependencies {
    implementation(project(":repo"))
    implementation(project(":core:common"))
    implementation(project(":core:supabase"))

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.core.splashscreen)


    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.navigation.fragment)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)

    // Tooling support (Previews, etc.)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.animation)

    // Material Design
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowSizeClass)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)



    implementation(libs.timber)
    implementation(libs.accompanist.permissions)

    // Camera
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


fun getEnvNullable(variableName: String): String {
    return System.getenv(variableName)
        .let {
            if (it?.isNotEmpty() == true) {
                "$it"
            } else {
                "\"\""
            }
        }
}

fun getPropertyOrEnvNullable(variableName: String, keystoreProperties: Properties): String {
    val variable = keystoreProperties[variableName] as String?

    val output = variable ?: getEnvNullable(variableName)

    return output
}
