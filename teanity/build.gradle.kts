plugins {
    id("com.android.library")

    id("kotlin-android")
    id("kotlin-kapt")

    id("com.github.dcendents.android-maven")
}

val group = "com.github.skoumalcz"

android {
    compileSdkVersion(Config.Android.compileSdk)

    defaultConfig {
        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(Config.Build.Type.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    //testImplementation("junit", "junit", "4.12")
    //androidTestImplementation("androidx.test", "runner", "1.1.1")
    //androidTestImplementation("androidx.test.espresso", "espresso-core", "3.1.1")

    implementation(kotlin("stdlib-jdk7", Config.Dependency.kotlin))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-android", Config.Dependency.kotlinCoroutines)

    /* === SUPPORT === */
    api("androidx.appcompat", "appcompat", Config.Dependency.appcompat)

    /* === CORE === */
    api("androidx.core", "core-ktx", Config.Dependency.ktx)
    api("androidx.lifecycle", "lifecycle-extensions", Config.Dependency.lifecycle)
    api("androidx.lifecycle", "lifecycle-viewmodel-ktx", Config.Dependency.lifecycle)
    api("androidx.navigation", "navigation-fragment-ktx", Config.Dependency.navigation)
    api("androidx.navigation", "navigation-ui-ktx", Config.Dependency.navigation)
    api("androidx.work", "work-runtime-ktx", Config.Dependency.work)

    /* === DB === */
    api("androidx.room", "room-ktx", Config.Dependency.room)

    /* === RX === */
    api("io.reactivex.rxjava2", "rxkotlin", Config.Dependency.rxkotlin)
    api("io.reactivex.rxjava2", "rxandroid", Config.Dependency.rxandroid)

    /* === RETROFIT === */
    compileOnly("com.squareup.retrofit2", "retrofit", Config.Dependency.retrofit)

    /* === EASING === */
    api("com.evernote", "android-state", Config.Dependency.state)
    api("com.karumi", "dexter", Config.Dependency.dexter)
    api("me.tatarka.bindingcollectionadapter2", "bindingcollectionadapter", Config.Dependency.bca)
    api("me.tatarka.bindingcollectionadapter2", "bindingcollectionadapter-recyclerview", Config.Dependency.bca)
    api("wiki.depasquale", "response-sanitizer", Config.Dependency.sanitizer)

    /* === UI === */
    api("com.github.bumptech.glide", "glide", Config.Dependency.glide)
    api("androidx.dynamicanimation", "dynamicanimation", Config.Dependency.animation)
    api("androidx.dynamicanimation", "dynamicanimation-ktx", Config.Dependency.animationKtx)
}

apply(from = "groovy.gradle")