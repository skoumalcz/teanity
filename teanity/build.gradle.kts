plugins {
    id("com.android.library")

    id("kotlin-android")
    id("kotlin-kapt")

    id("com.github.dcendents.android-maven")
}

val group = "com.github.skoumalcz"

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    val versions: Map<String, String> by rootProject.extra

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions["kotlin"]}")

    /* === SUPPORT === */
    api("androidx.appcompat:appcompat:${versions["appcompat"]}")

    /* === CORE === */
    api("androidx.core:core-ktx:${versions["ktx"]}")
    api("androidx.lifecycle:lifecycle-extensions:${versions["lifecycle"]}")
    api("android.arch.navigation:navigation-fragment-ktx:${versions["navigation"]}")
    api("android.arch.navigation:navigation-ui-ktx:${versions["navigation"]}")
    api("android.arch.work:work-runtime-ktx:${versions["work"]}")

    /* === DB === */
    api("androidx.room:room-runtime:${versions["room"]}")

    /* === RX === */
    api("io.reactivex.rxjava2:rxkotlin:${versions["rxkotlin"]}")
    api("io.reactivex.rxjava2:rxandroid:${versions["rxandroid"]}")

    /* === EASING === */
    api("com.evernote:android-state:${versions["state"]}")
    api("com.karumi:dexter:${versions["dexter"]}")
    api("me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:${versions["bca"]}")
    api("me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:${versions["bca"]}")

    /* === UI === */
    api("com.github.bumptech.glide:glide:${versions["glide"]}")
}

apply(from = "groovy.gradle")