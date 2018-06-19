# teanity

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
