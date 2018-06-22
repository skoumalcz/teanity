package com.skoumal.teanity.database

import android.arch.persistence.room.*

interface BaseDao<T> {

    @Delete
    fun delete(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objects: List<T>): List<Long>

    @Update
    fun update(obj: T): Int

    @Transaction
    @Update
    fun update(objects: List<T>): Int

    /**
     * Requires proper annotation that deletes the data. If not overridden in DAO it's rendering
     * itself useless.
     *
     * Example:
     * `@Query("DELETE FROM channel")`
     */
    fun deleteAll()

    /**
     * Requires proper annotation that fetches the data. If not overridden in DAO it's rendering
     * itself useless.
     *
     * Example:
     * `@Query("SELECT * FROM channel")`
     */
    fun fetchAll(): List<T>
}