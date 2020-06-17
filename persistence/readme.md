<p align="center">
  <img src="../art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

# Persistence Module

Apart from providing dependencies, it provides BaseDao which reduces overall dao footprint in your
apps.

## BaseDao

Room automatically generates implementations for inherited stubs as well, so you can leverage that
capability by simply implementing this interface.

### Example

```kotlin
@Dao
interface FooDao : BaseDao<User> {

    @Query("...")
    suspend fun selectUserById(id: String): User?

    @Query("...")
    suspend fun observeUserById(id: String): LiveData<User?>

}
```