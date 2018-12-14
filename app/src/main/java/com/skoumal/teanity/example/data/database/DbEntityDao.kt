package com.skoumal.teanity.example.data.database

import androidx.room.Dao
import androidx.room.Query
import com.skoumal.teanity.database.BaseDao
import com.skoumal.teanity.example.model.entity.DbEntity

@Dao
interface DbEntityDao : BaseDao<DbEntity> {

    @Query("DELETE from entity")
    override fun deleteAll()

    @Query("SELECT * from entity")
    override fun fetchAll(): List<DbEntity>

}