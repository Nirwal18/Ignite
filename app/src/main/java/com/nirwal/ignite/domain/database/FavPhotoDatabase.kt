package com.nirwal.ignite.domain.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [PhotoEntity::class, SearchHistoryEntity::class],
    version = 1,

)
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
                    )
                        //.addMigrations(migration_1_2)
                        .build()
                    INSTANCE = instance
                    instance
                }



        }

        /*

        private val migration_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database){
                    //Creating backup table
                    execSQL(
                        "CREATE TABLE PhotoEntity_backup " +
                                "(remoteId INTEGER," +
                                " photographer TEXT," +
                                " photographerId INTEGER," +
                                " alt TEXT," +
                                " original_url TEXT," +
                                " small_url TEXT," +
                                " medium_url TEXT," +
                                " large_url TEXT," +
                                " PRIMARY KEY (remoteId))"
                    )

                    //insert data from old table to backup table
                    execSQL("INSERT INTO PhotoEntity_backup SELECT  remoteId, photographer, photographerId, alt, original_url, small_url, medium_url, large_url FROM PhotoEntity")
                    //delete old table
                    execSQL("DROP TABLE PhotoEntity")
                    //rename backup table
                    execSQL("ALTER TABLE PhotoEntity_backup RENAME to PhotoEntity")
                }
            }

        }

*/

    }
    abstract fun photoDao(): FavouritePhotoDao

    abstract fun searchHistoryDao():SearchHistoryDao


}

