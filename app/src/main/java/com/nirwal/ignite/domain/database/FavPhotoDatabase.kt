package com.nirwal.ignite.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class FavPhotoDatabase : RoomDatabase() {
    companion object{
        @Volatile
        var INSTANCE:FavPhotoDatabase? = null

        fun getInstance(context:Context): FavPhotoDatabase {
            return INSTANCE
                ?:synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context,
                        FavPhotoDatabase::class.java, "favourite_photo_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }



        }
    }
    abstract fun photoDao(): FavouritePhotoDao
}

