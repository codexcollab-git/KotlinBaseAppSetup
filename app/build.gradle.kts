plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kt.ksp)
    alias(libs.plugins.kt.parcelize)
    /*alias(libs.plugins.google.services)*/
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.lokesh.appsetup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lokesh.appsetup"
        minSdk = 24
        targetSdk = 34
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

        buildConfigField("boolean", "IS_CURRENT_FLAVOUR_DEV", "false")

        // Room Schema Builder
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // signingConfig signingConfigs.release
        }
    }

    val develop_versionCode = 1
    val staging_versionCode = 1

    flavorDimensions += "version"
    productFlavors {
        create("develop") {
            dimension = "version"
            applicationIdSuffix = ".develop"
            versionNameSuffix = "-develop"
            versionCode = develop_versionCode
            buildConfigField("boolean", "IS_CURRENT_FLAVOUR_DEV", "true")
        }
        create("staging") {
            dimension = "version"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            versionCode = staging_versionCode
            buildConfigField("boolean", "IS_CURRENT_FLAVOUR_DEV", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    lint {
        checkReleaseBuilds = false
    }
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ktlint {
    android.set(true)
    debug.set(true)
    verbose.set(true)
    ignoreFailures.set(true)
    outputToConsole.set(true)
    disabledRules.set(listOf("import-ordering", "final-newline", "no-wildcard-imports"))
}

dependencies {
    /** Core */
    implementation(libs.bundles.kotlin.test)
    androidTestImplementation(libs.bundles.kotlin.test)
    implementation(libs.bundles.kotlin.test.additional)
    implementation(libs.bundles.androidx.core)
    implementation(libs.bundles.androidx.annotation)
    implementation(libs.bundles.androidx.arch.core)

    /** Activity */
    implementation(libs.bundles.androidx.activity)

    /** Navigation */
    implementation(libs.bundles.androidx.navigation)

    /** Collection */
    implementation(libs.bundles.androidx.collection)

    /** Lifecycle */
    implementation(libs.bundles.androidx.lifecycle.common)
    implementation(libs.bundles.androidx.lifecycle.runtime)
    implementation(libs.bundles.androidx.lifecycle.livedata)
    implementation(libs.bundles.androidx.lifecycle.viewmodel)

    /** Compose */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.androidx.compose.runtime)
    implementation(libs.bundles.androidx.compose.graphics)
    implementation(libs.bundles.androidx.compose.test)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    /** Work-manager */
    implementation(libs.bundles.androidx.work)

    /** Coroutines */
    implementation(libs.bundles.kotlin.coroutines)

    /** Hilt-DI */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    /** Retrofit */
    implementation(libs.bundles.retrofit)

    /** Room */
    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)

    /** UI-Pixels */
    implementation(libs.bundles.pixel)

    /** Firebase */
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.all)

    /** App-Update */
    implementation(libs.play.core)

    /** Permission */
    implementation(libs.permission.dexter)

    /** DataStore */
    implementation(libs.androidx.datastore)

    /** Glide */
    implementation(libs.image.loading.glide)
    ksp(libs.image.loading.glide.compiler)

    /** Coil */
    implementation(libs.bundles.image.loading.coil)
}
