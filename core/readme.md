<p align="center">
  <img src="../art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

# Core Module

This is our ever changing collection of tools, helpers and base structures. It provides guidance
on how to use modern android, handle communication between viewModel and views, bind views and
much more.

## Views (a.k.a. where to start)

We, of course, provide base views and viewModels for your (and our) convenience. We currently
support `Activity`, `Fragment` and `DialogFragment`. They all come pre-equipped with:

- support for **system window insets**
- unidirectional communication between viewModel and view
- support for snackbars
- support for navController

### Example

Example covers basic implementation of views with [navigation](https://developer.android.com/guide/navigation/navigation-getting-started)
library enabled.

```kotlin
class ExampleViewModel : TeanityViewModel() {

    fun navigateToHome() = // supports navigation from within viewModels
        ExampleFragmentDirections.navigateHome().publish()

    suspend fun hasCameraPermission() = // supports awaiting for activity contracts
        ActivityResultContracts.RequestPermission().await(Manifest.permission.CAMERA)

}
```

```kotlin
class ExampleActivity : TeanityActivity<ExampleViewModel, ActivityExampleBinding>() {

    override val layoutRes = R.layout.activity_example
    override val viewModel by lifecycleScope.viewModel<ExampleViewModel>() // uses Koin DI

    override val navHostId = R.id.example_nav_host

}
```

```kotlin
class ExampleFragment : TeanityFragment<ExampleViewModel, FragmentExampleBinding>() {

    override val layoutRes = R.layout.fragment_example
    override val viewModel by sharedViewModel<ExampleViewModel>() // uses Koin DI

}
```

## ViewModels

ViewModels are essential part of your app at this point. With that there's probably not a single
viewModel type that would fit all of your needs, so we provide 3 essential ones.

### Example

`TeanityViewModel` contains all the necessary extensions of normal viewModels you'll love. Starting
from ordered refreshes to communication to your view. It allows you to observe live data until is
the viewModel cleared and to create observable properties with native delegation!

```kotlin
class ExampleViewModel(
    private val fooBar: FooBarUseCase
) : TeanityViewModel() {

    @get:Bindable
    var isFoo by observable(false, BR.foo)
        private set

    init {
        fooBar.observe().observe {
            // observe until the viewModel dies
        }
    }

    fun navigateToHome() =
        ExampleFragmentDirections.navigateHome().publish()

    suspend fun hasCameraPermission() =
        ActivityResultContracts.RequestPermission().await(Manifest.permission.CAMERA)

    override suspend fun refresh() {
        delay(3000) // long running task
    }

}
```

`LoadingViewModel` allows you to launch loading tasks.

```kotlin
class ExampleLoadingViewModel : LoadingViewModel() {

    init {
        loading {
            delay(3000)
        }
    }

}
```

`LiveStateViewModel` includes a stateful live data and so discourages you from using built-in
"observable" features of the base `TeanityViewModel`. All data handled within this viewModel should
be enclosed in sealed state you provide.

```kotlin
sealed class ExampleState {

    object Loading : ExampleState()

    data class LoadedStage1(
        val title: String,
        val subtitle: String,
        val message: String
    ) : ExampleState()

    data class LoadedStage2(
        val list: List<RecyclerViewItem>,
        val diff: DiffUtil.DiffResult
    ) : ExampleState()

}

class ExampleLiveViewModel : LiveStateViewModel<ExampleState>(ExampleState.Loading)
```

## Lists

Aren't recycler view adapters annoying? We're here to the rescue. No more annoying adapters for
RecyclerView. Create one that does everything for you, bind variables and focus on your UI.

### Example

```kotlin
class FooBar {

    private val adapter = BindingAdapter<RecyclerViewItem>(viewLifecycleOwner) {
        it.setVariable(BR.viewModel, viewModel)
    }

    fun refreshList(newList: List<RecyclerViewItem>) {
        scope.launch(Dispatchers.Main) {
            val callback = adapter.getCallbackFrom(newList)
            val diff = withContext(Dispatchers.Default) {
                DiffUtil.calculateDiff(callback)
            }
            adapter.update(newList, diff)
        }
    }

}
```

## Custom View Events

One will surely need to communicate to the view that custom event happened and ordering it to do
something. We thought of that and `ViewEvent`s with `*Executor`s are here to help. To execute them
automatically, please mind that your View needs to extend either of `Teanity*` Views, otherwise
you'd need to manage those events yourself. Once created an instance of the `ViewEvent` call
`.publish()` on them from your `TeanityViewModel`.

### Example

You can essentially define your `ViewEvent`s in two major ways. Inner or global events. Example
covers both of these use cases.

#### Inner

Event is strictly prohibited from being used by any other view than this one. Private methods can
be used like that, hence not exposing them and having inner event might be more desirable in some
cases.

```kotlin
class FooFragment : TeanityFragment() {

    private fun moveView(which: Int) = TODO()

    class AnimateViewEvent(
        private val which: Int
    ) : ViewEvent(), FragmentExecutor {

        override fun invoke(fragment: Fragment) {
            if (fragment !is FooFragment) return
            fragment.moveView(which)
        }

    }

}
```

#### Global

Event can be run from anywhere, anytime. Generally should not be picky about instance of the
fragment or activity.

```kotlin
class OpenUriEvent(
    private val uri: Uri
) : ViewEvent(), ContextExecutor {

    override fun invoke(context: Context) {
        TODO("open uri")
    }

}
```

