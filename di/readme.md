<p align="center">
  <img src="../art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

# DI Module

It provides all the necessary dependencies to jumpstart your DI game. Teanity recommends
[Koin](https://insert-koin.io/) dependency injection.

## Additional definitions

We all use system services - lots of them regularly. If you start your koin via teanity they come
predefined, ready to be used.

### Example

```kotlin
class App : Application {

    override fun onCreate() {
        // ...
        Teanity.startWith(this, listOf(myModule, myOtherModule))
    }

}
```