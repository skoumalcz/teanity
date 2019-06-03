import com.android.build.gradle.internal.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.SigningConfig

plugins {
    id("com.android.application")

    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")

    id("io.fabric")

    id("androidx.navigation.safeargs.kotlin")
}

repositories {
    google()
    jcenter()
    maven(url = "https://maven.fabric.io/public")
    maven(url = "https://jitpack.io")
    maven(url = "http://oss.sonatype.org/content/repositories/snapshots")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
    mapDiagnosticLocations = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("moshi.generated", "javax.annotation.Generated")
    }
    javacOptions {
        option("-Xmaxerrs", 1000)
    }
}

android {
    compileSdkVersion(Config.Android.compileSdk)

    defaultConfig {
        applicationId = "com.skoumal.teanity.example"
        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)
        versionCode = 1
        versionName = "1.0"

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    signingConfigs {

        fun createChild(name: String, builder: SigningConfig.() -> Unit) = create(name) {
            initWith(getByName(Config.Sign.DEFAULT))
            builder()
        }

        create(Config.Sign.DEFAULT) {
            isV1SigningEnabled = true
            isV2SigningEnabled = true
            //storeFile = File("keystore.jks")
            //storePassword = "yourpassword"
        }

        createChild(Config.Sign.DEBUG) {
            //keyAlias = "debug"
            //keyPassword = "yourpassword"
        }
        createChild(Config.Sign.ALPHA) {
            //keyAlias = "alpha"
            //keyPassword = "yourpassword"
        }
        createChild(Config.Sign.BETA) {
            //keyAlias = "beta"
            //keyPassword = "yourpassword"
        }
    }

    buildTypes {
        getByName(Config.Build.Type.RELEASE) {
            isMinifyEnabled = true
            isUseProguard = true
            isCrunchPngs = true
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName(Config.Build.Type.DEBUG) {
            signingConfig = signingConfigs.findByName(Config.Sign.DEBUG)
        }
        create(Config.Build.Type.ALPHA) {
            matchingFallbacks = listOf(Config.Build.Type.DEBUG)
            applicationIdSuffix = ".${Config.Build.Type.ALPHA}"
        }
        create(Config.Build.Type.BETA) {
            matchingFallbacks = listOf(Config.Build.Type.RELEASE)
            applicationIdSuffix = ".${Config.Build.Type.BETA}"
        }
    }

    flavorDimensions(Config.Build.Dimen.DEFAULT)

    productFlavors {
        fun createDefault(name: String, builder: ProductFlavor.() -> Unit) = create(name) {
            setDimension(Config.Build.Dimen.DEFAULT)
            builder()
        }

        /*createDefault(Config.Build.VANILLA) {
            signingConfig = signingConfigs.findByName(Config.Sign.VANILLA_RELEASE)
        }
*/
        /*createDefault(Config.Build.CHOCOLATE) {
            signingConfig = signingConfigs.findByName(Config.Sign.CHOCOLATE_RELEASE)
            applicationIdSuffix = ".${Config.Build.CHOCOLATE}"
        }*/
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(project(":teanity"))

    // TESTING
    //testImplementation("junit", "junit", "4.12")
    //androidTestImplementation("androidx.test", "runner", "1.1.1")
    //androidTestImplementation("androidx.test.espresso", "espresso-core", "3.1.1")

    implementation("com.crashlytics.sdk.android", "crashlytics", Config.Dependency.crashlytics, ext = "aar") {
        isTransitive = true
    }
    implementation("com.facebook.stetho", "stetho", "1.5.0")
    implementation("com.android.support", "multidex", "1.0.3")

    // KOTLIN
    implementation(kotlin("stdlib-jdk7", version = Config.Dependency.kotlin))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", Config.Dependency.kotlinCoroutines)

    // SUPPORT LIBS
    implementation("com.google.android.material", "material", Config.Dependency.material)
    implementation("androidx.constraintlayout", "constraintlayout", Config.Dependency.constraintLayout)

    // DEPENDENCY INJECTION
    implementation("org.koin", "koin-core", Config.Dependency.koin)
    implementation("org.koin", "koin-android", Config.Dependency.koin)
    implementation("org.koin", "koin-androidx-viewmodel", Config.Dependency.koin)

    // NETWORKING
    implementation("com.jakewharton.retrofit", "retrofit2-kotlin-coroutines-adapter", Config.Dependency.retrofitCoroutines)
    implementation("com.squareup.retrofit2", "retrofit", Config.Dependency.retrofit)
    implementation("com.squareup.retrofit2", "converter-moshi", Config.Dependency.retrofit)
    implementation("com.squareup.retrofit2", "adapter-rxjava2", Config.Dependency.retrofit)
    implementation("com.squareup.okhttp3", "okhttp", Config.Dependency.okhttp)
    implementation("com.squareup.okhttp3", "logging-interceptor", Config.Dependency.okhttp)
    implementation("com.squareup.moshi", "moshi", Config.Dependency.moshi)
    // GLIDE
    implementation("com.github.bumptech.glide", "okhttp3-integration", Config.Dependency.glide, ext = "aar")

    // EASING
    implementation("com.chibatching.kotpref", "kotpref", Config.Dependency.kotpref)
    implementation("com.jakewharton.timber", "timber", Config.Dependency.timber)

    // KAPTs
    kapt("com.squareup.moshi", "moshi-kotlin-codegen", Config.Dependency.moshi)
    kapt("com.github.bumptech.glide", "compiler", Config.Dependency.glide)
    kapt("com.evernote", "android-state-processor", Config.Dependency.state)
    kapt("androidx.room", "room-compiler", Config.Dependency.room)
}
