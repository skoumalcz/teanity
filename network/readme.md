<p align="center">
  <img src="../art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

# Network Module

Module mainly provides transitive dependencies we usually use. On top of that it provides few
utilities to correctly handle exceptions and errors while performing API calls.

## Extensions

### Example

```kotlin
fun fooBar() {
    val response: retrofit2.Response<List<String>> = TODO()
    response
        .toResult() // replaces Response with kotlin.Result, error is marked as NetworkException
        .flatMap { it.toOtherList() } // transforms singular item to list and returns flattened list of lists
}
```