package com.skoumal.teanity.example.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.skoumal.teanity.example.model.entity.DbEntity

@Dao
interface DbEntityDao {

    @Query("SELECT * from entity")
    fun getAllEntities(): LiveData<List<DbEntity>>
}