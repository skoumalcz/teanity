# TEANITY #

Android-Kotlin "framework" designed to reduce boilerplate.

### Installation ###

[![](https://jitpack.io/v/skoumalcz/teanity.svg)](https://jitpack.io/#skoumalcz/teanity)

Project root build.gradle
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

dependency
```groovy
dependencies {
    implementation 'com.github.skoumalcz:teanity:latest.version'
}
```

#### Starting a new project? ####

Try our cl tools:

* [MAC / Linux](https://gist.githubusercontent.com/diareuse/d4ff8283b8cbc07b498a56af47d75ca7/raw/7763cc393e45e4c8e6033630de6f5329f532aac7/quickstart.sh)
    
* [Windows](https://gist.githubusercontent.com/diareuse/36b7aa4e544e1a47fdad999e493266dd/raw/5637e7cc2e7ad2041fd0d02301dbc395504fa1b5/quickstart.ps1)

#### Note

Android Studio will not probably find your project's classes and stuff. This is due to usage of Kotlin DSL in Gradle build files. To overcome this issue you'll need to create a empty `build.gradle` file and then click the "Sync project with Gradle files" button and the project will appear correctly.

# README #

Android "framework"

### Main libraries used ###

* [Android ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
	* Replacement for Nucleus
	
* [Koin](https://github.com/Ekito/koin)
	* Dependency injection, replacement for dagger
	* Kotlin DSL & delagated properties instead of annotations and code generation
	* No `MyApp.instance.inject(this)` in all activities/fragments, no reflection, no difficult Dagger Android setup
	* Direct support for android ViewModels constructor injection

* [Moshi](https://github.com/square/moshi)
	* JSON parser (adapter for retrofit), replacement for Gson
	* Generates adapters for your models - no reflection
	* Unlike Gson, throws if non-null property of model is missing in JSON
	* Read about it here: https://medium.com/@sweers/exploring-moshis-kotlin-code-gen-dec09d72de5e

* [Kotpref](https://github.com/chibatching/Kotpref)
	* Generates shared preferences, easier than SPG
	* Kotlin delegated properties instead of code generation - you can start using created preferences right away, without build

* [Room](https://developer.android.com/topic/libraries/architecture/room)
	* Sqlite wrapper
	* Example shows only basic usage, [pull requests welcome](https://www.urbandictionary.com/define.php?term=patches%20are%20welcome&defid=7833039)

* [Binding collection adapters](https://github.com/evant/binding-collection-adapter)
	* Easily bind list of entities to recycler view (list view, spinner, ...) items, adapters never more
	
* [Dexter](https://github.com/Karumi/Dexter)
	* (Relatively) easily handles runtime permissions
	
* [Navigation architecture components](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing)
	* Handles activity/fragment creation for you - no startActivity, no fragment transactions, just navigation graph
	* Currently in alpha, lot of things are missing
	* Probably not production ready now, but certainly way to go in the (hopefully near) future
	
* [Retrofit](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)
* [Timber](https://github.com/JakeWharton/timber)
