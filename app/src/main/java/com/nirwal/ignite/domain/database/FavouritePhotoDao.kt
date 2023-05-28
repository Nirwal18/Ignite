package com.nirwal.ignite.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouritePhotoDao {
    @Query("SELECT * FROM PhotoEntity")
    fun getAll(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM PhotoEntity WHERE remoteId IN (:photoId)")
    fun loadAllByIds(photoId: IntArray): List<PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE photographer LIKE :first ")
    fun findByAuthorName(first: String):PhotoEntity

    @Insert
    fun insert(photo: PhotoEntity)

    @Upsert
    fun upsert(photo: PhotoEntity)

    @Delete
    fun delete(user: PhotoEntity)
}