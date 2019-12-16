@Suppress("ClassName")
object Lib {

    private object V {
        const val kotlin = "1.3.61"
        const val kotlinCoroutines = "1.3.2"
        const val gradle = "3.6.0-beta04"

        object AndroidX {
            const val appcompat = "1.1.0"
            const val core = "1.2.0-rc01"
            const val room = "2.2.2"
            const val animation = "1.0.0-alpha02"
            const val constraint = "2.0.0-beta3"
            const val material = "1.1.0-beta02"
            const val lifecycle = "2.2.0-rc02"
            const val navigation = "2.2.0-rc02"
            const val test = "1.2.0"
            const val junit = "1.1.1"
            const val services = "1.2.0"
            const val automator = "2.2.0"
            const val espresso = "3.2.0"
            const val multidex = "2.0.1"
        }

        const val lorem = "2.1"
        const val rxKotlin = "2.4.0"
        const val rxAndroid = "2.1.1"
        const val binding = "3.1.1"
        const val koin = "2.0.1"
        const val moshi = "1.9.2"
        const val retrofit = "2.6.2"
        const val sanitizer = "0.6"
        const val glide = "4.10.0"
        const val dexter = "6.0.0"
        const val timber = "4.7.1"
        const val state = "1.4.1"
        // this is kinda deprecated
        const val maven = "2.1"
    }

    //region Access definitions
    val kotlin = Kotlin
    val androidx = AndroidX
    val lifecycle = Lifecycle
    val navigation = Navigation
    val reactive = ReactiveX
    val bindingCollection = BindingCollection
    val koin = Koin
    val square = Square
    val test = Test
    //endregion

    object Kotlin {
        val lib = kotlin("stdlib-jdk7", V.kotlin)
        val gradle = kotlin("gradle-plugin", V.kotlin)
        val coroutines = kotlinx("coroutines-android", V.kotlinCoroutines)
    }

    object AndroidX {
        val build = "com.android.tools.build:gradle:${V.gradle}"
        val appcompat = androidx("appcompat", "appcompat", V.AndroidX.appcompat)
        val core = androidx("core", "core-ktx", V.AndroidX.core)
        val room = androidx("room", "room-ktx", V.AndroidX.room)
        val animation =
            androidx("dynamicanimation", "dynamicanimation-ktx", V.AndroidX.animation)
        val constraint =
            androidx("constraintlayout", "constraintlayout", V.AndroidX.constraint)
        val material = "com.google.android.material:material:${V.AndroidX.material}"
        val multidex = androidx("multidex", "multidex", V.AndroidX.multidex)
    }

    object Lifecycle {
        val extensions = androidx("lifecycle", "lifecycle-extensions", V.AndroidX.lifecycle)
        val viewModel = androidx("lifecycle", "lifecycle-viewmodel-ktx", V.AndroidX.lifecycle)
    }

    object Navigation {
        val fragment =
            androidx("navigation", "navigation-fragment-ktx", V.AndroidX.navigation)
        val ui = androidx("navigation", "navigation-ui-ktx", V.AndroidX.navigation)
    }

    object ReactiveX {
        val kotlin = rxjava2("rxkotlin", V.rxKotlin)
        val android = rxjava2("rxandroid", V.rxAndroid)
    }

    object BindingCollection {
        val collections = bindingAdapter("bindingcollectionadapter", V.binding)
        val recycler = bindingAdapter("bindingcollectionadapter-recyclerview", V.binding)
    }

    object Koin {
        val core = koin("android", V.koin)
        val viewModel = koin("android-viewmodel", V.koin)
    }

    object Square {
        val moshi = square("moshi", "moshi-kotlin", V.moshi)
        val retrofit = square("retrofit2", "retrofit", V.retrofit)
        val moshiConverter = square("retrofit2", "converter-moshi", V.retrofit)
    }

    object Test {

        val core = androidx("test", "core", V.AndroidX.test)
        val runner = androidx("test", "runner", V.AndroidX.test)
        val rules = androidx("test", "rules", V.AndroidX.test)
        val junit = androidx("test.ext", "junit", V.AndroidX.junit)
        val services = androidx("test.services", "test-services", V.AndroidX.services)
        val automator = androidx("test.uiautomator", "uiautomator", V.AndroidX.automator)
        val lorem = "com.thedeanda:lorem:${V.lorem}"
        val espresso = Espresso

        object Espresso {
            val core = espresso("core", V.AndroidX.espresso)
            val contrib = espresso("contrib", V.AndroidX.espresso)
            val intents = espresso("intents", V.AndroidX.espresso)
            val web = espresso("web", V.AndroidX.espresso)
            val idling = androidx("test.espresso.idling", "idling-concurrent", V.AndroidX.espresso)
        }

    }

    val glide = github("bumptech.glide", "glide", V.glide)
    const val sanitizer = "wiki.depasquale:response-sanitizer:${V.sanitizer}"

    const val dexter = "com.karumi:dexter:${V.dexter}"
    const val timber = "com.jakewharton.timber:timber:${V.timber}"
    const val state = "com.evernote:android-state:${V.state}"
    const val maven = "com.github.dcendents:android-maven-gradle-plugin:${V.maven}"

    // ---

    private fun kotlin(module: String, version: String? = null) =
        "org.jetbrains.kotlin:kotlin-$module${version?.let { ":$version" } ?: ""}"

    private fun kotlinx(module: String, version: String? = null) =
        "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"

    private fun androidx(group: String, module: String, version: String) =
        "androidx.$group:$module:$version"

    private fun espresso(module: String, version: String) =
        androidx("test.espresso", "espresso-$module", version)

    private fun rxjava2(module: String, version: String) =
        "io.reactivex.rxjava2:$module:$version"

    private fun github(user: String, module: String, version: String) =
        "com.github.$user:$module:$version"

    private fun bindingAdapter(module: String, version: String) =
        "me.tatarka.bindingcollectionadapter2:$module:$version"

    private fun square(group: String, module: String, version: String) =
        "com.squareup.$group:$module:$version"

    private fun koin(module: String, version: String) =
        "org.koin:koin-$module:$version"
}

