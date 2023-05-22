package com.nirwal.ignite.domain.repositry

import io.ktor.client.HttpClient

class PhotoRepositoryImp(client:HttpClient):PhotoRepository {

    override fun getCuratedPhotos() {
        TODO("Not yet implemented")
    }

    override fun getPhoto(id: Int) {
        TODO("Not yet implemented")
    }

    override fun searchPhotos(query: String) {
        TODO("Not yet implemented")
    }
}