package com.nirwal.ignite.domain.repositry

import com.nirwal.ignite.common.Constant
import com.nirwal.ignite.common.Endpoints
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.model.PhotoResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson


class PhotoRepositoryImp():PhotoRepository {

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 60_000L*10L
        }

        install(ContentNegotiation) {
            gson()
        }

        install(Logging) {
            level = LogLevel.HEADERS
            logger = object :Logger{
                override fun log(message: String) {
                    println(message)
                }
            }

        }
        defaultRequest {
            header("Authorization", Constant.PIXEL_API_KEY)
        }

    }

    override suspend fun getCuratedPhotos(page:Int, itemCount:Int):PhotoResult? {
        return  try {
            client.get(Endpoints.curatedPhotos){
                url {
                    parameters.append("page", page.toString())
                    parameters.append("per_page", itemCount.toString())
                }
            }.body()
        }catch (e:HttpRequestTimeoutException){
            println(e.message)
            null
        }
    }

    override suspend fun getPhoto(id: Int): Photo? {
       return try {
           client.get("${Endpoints.GET_PHOTO_BY_ID}$id"){

           }.body()
       }catch (e:Exception){
           println(e.message)
           null
       }
    }

    override fun searchPhotos(query: String) {
        TODO("Not yet implemented")
    }
}