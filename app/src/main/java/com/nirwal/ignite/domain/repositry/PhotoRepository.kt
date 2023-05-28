package com.nirwal.ignite.domain.repositry

import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.model.PhotoResult

interface PhotoRepository {
    suspend fun getCuratedPhotos(page:Int, itemCount:Int): PhotoResult?
    suspend fun getPhoto(id:Int): Photo?
    fun searchPhotos(query:String)
}