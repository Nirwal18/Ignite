package com.nirwal.ignite.domain.repositry

interface PhotoRepository {
    fun getCuratedPhotos()
    fun getPhoto(id:Int)
    fun searchPhotos(query:String)
}