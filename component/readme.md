<p align="center">
  <img src="../art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

# Component Module

Component module is our effort to provide arch tools for cleaner and more importantly testable,
isolated code of your applications.

## [Use-Case](src/main/java/com/skoumal/teanity/component/CompoundUseCase.kt)

We isolate our execution logic into UseCases with singular purpose. That way you get amazing
modularity but at expense of generating (a small amount*) of boilerplate. These UseCases can be
furthermore merged in one "master" UseCase - assembling complex logic, passing arguments to each
other, etc...

_* "a small amount" means in this case a class and typically one method override._

### Example

Example covers usage of 4 use cases, typically used while trying to fetch user data from remote,
saving them to the internal database and ultimately observing them in the UI.

_`$projectDir/model/User.kt`_
```kotlin
typealias UserId = String

data class User(
    val id: UserId,
    val name: String
    //...
)
```

_`$projectDir/network/GetUserUseCase.kt`_
```kotlin
/**
 * Notice we use `Get*Foo*UseCase` pattern here.
 * If we were to PATCH some endpoint, then it would be `Patch*Foo*UseCase` and so forth.
 *
 * Also notice the constructors. We DO NOT(!) want these classes to be constructable beyond the
 * originating module. Consider making your class `internal` if its main usage is only inside the
 * originating module!
 *
 * Always propagate your UseCases through Dependency Injection as simple Singletons. Multiple
 * instances would likely be undesirable. (* Unless you specifically know what you're doing)
 * */
class GetUserUseCase internal constructor(
    private val api: MyService
) : CompoundUseCase<UserId, User>() {

    override suspend fun execute(input: UserId): User {
        // this might throw an exception at some point, but no worries.
        // the UseCase catches all errors and returns ComponentResult<User> that can be processed in
        // whichever way you want
        return api.getUserById(input)
    }

}
```

_`$projectDir/persistence/InsertUserUseCase.kt`_
```kotlin
/**
 * Same here, always name your UseCases accordingly! We insert to the database so the scheme is
 * `Insert*Foo*UseCase`.
 * */
class InsertUserUseCase internal constructor(
    private val dao: UserDao
) : CompoundUseCase<User, Unit>() {

    override suspend fun execute(input: User) {
        dao.insert(input) // can throw exceptions, but no worries here too
    }

}
```

_`$projectDir/persistence/SelectUserUseCase.kt`_
```kotlin
/**
 * And lastly identical here, user is being selected from database hence `Select*Foo*UseCase`.
 * */
class SelectUserUseCase internal constructor(
    private val dao: UserDao
) : CompoundUseCase<UserId, User>() {

    override fun observe(params: UserId): LiveData<User?> {
        // beware of throwing exceptions here though!
        // the UseCase is unable to catch anything since it would be highly inconvenient. Consider
        // catching the exceptions upstream and sending yourself a null instead.
        return dao.observe(params)
    }

    override suspend fun execute(input: UserId): User {
        return dao.select(input) // can throw exceptions, but no worries here too
    }

}
```

_`$projectDir/data/GetUserByIdUseCase.kt`_
```kotlin
/**
 * This UseCase will be used inside viewModels, views, etc... We need this name to be **more**
 * specific. Hence we choose to name them by scheme `{Action}{Model}{ByParameter}UseCase`
 * */
class GetUserByIdUseCase internal constructor(
    private val fetch: GetUserUseCase,
    private val insert: InsertUserUseCase,
    private val select: SelectUserUseCase
) : CompoundUseCase<UserId, User>() {

    override fun observe(input: UserId): LiveData<User?> {
        return select.observe(input)
    }

    override suspend fun execute(input: UserId): User {
        refresh(input)
        return select(input).getOrThrow()
    }

    /**
     * This function will never throw exceptions since it's consisting only of exception-protected
     * UseCases.
     * */
    private suspend fun refresh(input: UserId) {
        fetch(input).onSuccess {
            insert(it)
        }
    }

}
```

_`$projectDir/data/build.gradle`_
```groovy
// ...

dependencies {
	// you might want to hide the UseCases defined in network and persistence from the "user"
	// modules, so by only implementing them, they will be hidden and only those defined in `data`
	// module are visible.
	implementation(project(":network"))
	implementation(project(":persistence"))
}
```

## [Vessel](channel/Vessel.kt)

Communication between different parts of your app might be difficult. We created Vessel to help
with that. It's based on coroutines' Channel and is dead simple to use, implement and maintain.

### Example

Example covers usage of Vessel on communication between ViewModel and View.

```kotlin
open class ExampleViewModel : ViewModel() {

    val vessel = Vessel<Command>()

    protected fun Command.sail() = vessel.sail(this)

    sealed class Command : Vessel.Sailor {

        data class OpenUri(
            val uri: Uri
        ) : Command()

    }

}
```

```kotlin
class ExampleFragment : Fragment() {

    private val viewModel: ExampleViewModel by viewModels()

    // ...

    init {
        lifecycleScope.launchWhenCreated {
            viewModel.vessel.dock().collect {
                // todo handle command
            }
        }
    }

}
```