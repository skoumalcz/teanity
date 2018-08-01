package com.skoumal.teanity.example.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.skoumal.teanity.example.model.entity.DbEntity

@Dao
interface DbEntityDao {

    @Query("SELECT * from entity")
    fun getAllEntities(): LiveData<List<DbEntity>>
}