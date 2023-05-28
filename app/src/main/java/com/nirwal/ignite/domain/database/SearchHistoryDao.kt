package com.nirwal.ignite.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM SearchHistoryEntity")
    fun getAll(): Flow<List<SearchHistoryEntity>>

    @Insert
    fun insert(history: SearchHistoryEntity)

    @Upsert
    fun upsert(history: SearchHistoryEntity)

    @Delete
    fun delete(history: SearchHistoryEntity)
}