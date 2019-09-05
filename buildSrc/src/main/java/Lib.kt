@Suppress("ClassName")
object Lib {

    val kotlin = Kotlin

    object Kotlin {
        private const val version = "1.3.50"
        private const val versionCoroutines = "1.2.2"

        val lib = kotlin("stdlib-jdk7", version)
        val gradle = kotlin("gradle-plugin", version)
        val coroutines = kotlinx("coroutines-android", versionCoroutines)
    }

    val androidx = AndroidX

    object AndroidX {
        val build = "com.android.tools.build:gradle:3.5.0"
        val appcompat = androidx("appcompat", "appcompat", "1.1.0-rc01")
        val core = androidx("core", "core-ktx", "1.2.0-alpha02")
        val room = androidx("room", "room-ktx", "2.1.0")
        val animation = androidx("dynamicanimation", "dynamicanimation-ktx", "1.0.0-alpha02")
        val constraint = androidx("constraintlayout", "constraintlayout", "2.0.0-beta2")
        val material = "com.google.android.material:material:1.1.0-alpha09"
    }

    val lifecycle = Lifecycle

    object Lifecycle {
        private const val version = "2.2.0-alpha02"

        val extensions = androidx("lifecycle", "lifecycle-extensions", version)
        val viewModel = androidx("lifecycle", "lifecycle-viewmodel-ktx", version)
    }

    val navigation = Navigation

    object Navigation {
        private const val version = "2.1.0-alpha06"

        val fragment = androidx("navigation", "navigation-fragment-ktx", version)
        val ui = androidx("navigation", "navigation-ui-ktx", version)
    }

    val reactive = ReactiveX

    object ReactiveX {
        val kotlin = rxjava2("rxkotlin", "2.3.0")
        val android = rxjava2("rxandroid", "2.1.1")
    }

    // please do not update; nested binding fails to resolve on newer versions
    val bindingCollection = BindingCollection

    object BindingCollection {
        private const val version = "3.0.0-beta1"

        val collections = bindingAdapter("bindingcollectionadapter", version)
        val recycler = bindingAdapter("bindingcollectionadapter-recyclerview", version)
    }

    val koin = Koin

    object Koin {
        private const val version = "2.0.1"

        val core = koin("android", version)
        val viewModel = koin("android-viewmodel", version)
    }

    val square = Square

    object Square {
        private const val version = "2.6.1"

        val moshi = square("moshi", "moshi-kotlin", "1.8.0")
        val retrofit = square("retrofit2", "retrofit", version)
        val moshiConverter = square("retrofit2", "converter-moshi", version)
    }

    val sanitizer = github("diareuse", "response-sanitizer", "0.4")
    val glide = github("bumptech.glide", "glide", "4.9.0")

    const val dexter = "com.karumi:dexter:5.0.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val state = "com.evernote:android-state:1.4.1"
    const val maven = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    // ---

    val test = Test

    object Test {

        val core = androidx("test", "core", "1.2.0")
        val runner = androidx("test", "runner", "1.2.0")
        val rules = androidx("test", "rules", "1.2.0")
        val junit = androidx("test.ext", "junit", "1.1.1")
        val services = androidx("test.services", "test-services", "1.2.0")
        val automator = androidx("test.uiautomator", "uiautomator", "2.2.0")
        val lorem = "com.thedeanda:lorem:2.1"
        val espresso = Espresso

        object Espresso {

            private const val version = "3.2.0"

            val core = androidx("test.espresso", "espresso-core", version)
            val contrib = androidx("test.espresso", "espresso-contrib", version)
            val intents = androidx("test.espresso", "espresso-intents", version)
            val web = androidx("test.espresso", "espresso-web", version)
            val idling = androidx("test.espresso.idling", "idling-concurrent", version)

        }

    }

    // ---

    private fun kotlin(module: String, version: String? = null) =
        "org.jetbrains.kotlin:kotlin-$module${version?.let { ":$version" } ?: ""}"

    private fun kotlinx(module: String, version: String? = null) =
        "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"

    private fun androidx(group: String, module: String, version: String) =
        "androidx.$group:$module:$version"

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

