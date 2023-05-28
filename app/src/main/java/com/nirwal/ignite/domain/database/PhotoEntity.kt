package com.nirwal.ignite.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name ="remoteId")       val remoteId: Int = 0,
    @ColumnInfo(name = "photographer")  val photographer: String?,
    @ColumnInfo(name = "photographerId")val photographerId: Int?,
    @ColumnInfo(name = "alt")           val alt: String?,
    @ColumnInfo(name = "original_url")  val originalUrl: String?,
    @ColumnInfo(name = "small_url")     val smallUrl:String?,
    @ColumnInfo(name = "medium_url")    val largeUrl:String?,
    @ColumnInfo(name = "large_url")     val mediumUrl:String?,
    )