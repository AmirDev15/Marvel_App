import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
}


android {


    namespace = "com.example.marvel_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marvel_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }



        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))

            buildConfigField(
                "String",
                "PUBLIC_API_KEY",
                "\"${properties.getProperty("PUBLIC_API_KEY")}\""
            )
            buildConfigField(
                "String",
                "PRIVATE_KEY",
                "\"${properties.getProperty("PRIVATE_KEY")}\""
            )
        } else {
            buildConfigField("String", "PUBLIC_API_KEY", "\"\"")
            buildConfigField("String", "PRIVATE_KEY", "\"\"")
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true

        buildConfig=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.junit.junit)
    implementation(libs.engage.core)
    implementation(libs.androidx.room.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Room Database

    // Use the latest stable version
//
//    val  room_version = "2.1.0" // Use the latest version
//
//    implementation (libs.androidx.room.runtime)
//    annotationProcessor (libs.androidx.room.compiler)
//    kapt ("androidx.room:room-compiler:$room_version") // Only if using Kotlin


    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")
//
//    implementation("androidx.room:room-runtime:2.5.1")
//    implementation("androidx.room:room-ktx:2.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // Check for the latest version
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0") // Check for the latest version

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

//coil:

    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.google.accompanist:accompanist-coil:0.15.0")


    implementation("io.insert-koin:koin-android:3.3.0")
    implementation("io.insert-koin:koin-androidx-compose:3.3.0")


    // JUnit for unit testing
    testImplementation("junit:junit:4.13.2")

    // Mockito for mocking
    testImplementation("org.mockito:mockito-core:4.11.0")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")


    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("app.cash.turbine:turbine:0.7.0")
    // For Kotlin coroutine testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")


    //navigations

    testImplementation("org.mockito:mockito-inline:4.8.1") // use the latest version

    implementation("androidx.navigation:navigation-compose:2.7.2") // Use the latest version

//    kapt ("android.arch.persistence.room:compiler:1.1.1")
}