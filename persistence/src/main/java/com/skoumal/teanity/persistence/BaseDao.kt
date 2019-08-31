package com.skoumal.teanity.persistence

import androidx.room.*

interface BaseDao<T> {

    @Delete
    suspend fun delete(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(objects: List<T>): List<Long>

    @Update
    suspend fun update(obj: T): Int

    @Transaction
    @Update
    suspend fun update(objects: List<T>): Int

    /**
     * Requires proper annotation that deletes the data. If not overridden in DAO it's rendering
     * itself useless.
     *
     * Example:
     * `@Query("DELETE FROM channel")`
     */
    //fun deleteAll()

    /**
     * Requires proper annotation that fetches the data. If not overridden in DAO it's rendering
     * itself useless.
     *
     * Example:
     * `@Query("SELECT * FROM channel")`
     */
    //fun fetchAll(): List<T>
}