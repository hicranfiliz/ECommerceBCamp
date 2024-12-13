import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

val keystoreProperties = Properties().apply {
    val keyStoreFile = rootProject.file("keystore.properties")
    if (keyStoreFile.exists()){
        load(keyStoreFile.inputStream())
    }
}

android {
    namespace = "com.example.ecommercebcamp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ecommercebcamp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt{
            arguments {
                arg("room.schemaLocation","$projectDir/schemas")
            }
        }
    }

    signingConfigs {
        getByName("debug"){
            //keyAlias = "debug"
            //keyPassword = "android"
            //storeFile = file("${System.getProperty("user.home")}/Documents/keystores/keystore1.jks")
            //storePassword = "android"
        }
        create("release"){
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".test"
            versionNameSuffix = "_test"
            resValue("string", "app_name", "ECommerceApp_Test")
            buildConfigField("String", "BASE_URL", "\"https://run.mocky.io/v3/\"")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            applicationIdSuffix =".prod"
            versionNameSuffix = "_prod"
            resValue("string", "app_name", "ECommerceApp_Prod")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://run.mocky.io/v3/\"")
        }
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
        buildConfig = true
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
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    implementation(libs.android.gif.drawable)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.glide)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.lifecycle.livedata.ktx.v261)

    //lottie
    implementation(libs.lottie)
}