package com.nirwal.ignite.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavouritePhotoDao {
    @Query("SELECT * FROM PhotoEntity")
    fun getAll(): List<PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE uid IN (:photoId)")
    fun loadAllByIds(photoId: IntArray): List<PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE photographer LIKE :first ")
    fun findByAuthorName(first: String):PhotoEntity

    @Insert
    fun insert(photo: PhotoEntity)

    @Delete
    fun delete(user: PhotoEntity)
}