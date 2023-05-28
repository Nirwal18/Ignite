package com.nirwal.ignite.domain.repositry

import android.util.Log
import com.nirwal.ignite.common.Constant
import com.nirwal.ignite.common.Endpoints
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.model.PhotoResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import org.koin.core.component.getScopeName


class PhotoRepositoryImp():PhotoRepository {
    val TAG:String = "PhotoRepositoryImp"
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
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:ServerResponseException){
            Log.d(TAG, "getCuratedPhotos: Exception:Server response${e.response.status}")
            null
        }catch (e:ClientRequestException){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:RedirectResponseException){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:Exception){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }
    }

    override suspend fun getPhoto(id: Int): Photo? {
       return try {
           client.get("${Endpoints.GET_PHOTO_BY_ID}$id").body()
       }catch (e:Exception){
           println(e.message)
           null
       }
    }

    override suspend fun searchPhotos(query: String, page: Int, itemCount: Int):PhotoResult? {
        return  try {
            client.get(Endpoints.SEARCH_URL){
                url {
                    parameters.append("page", page.toString())
                    parameters.append("per_page", itemCount.toString())
                }
            }.body()
        }catch (e:HttpRequestTimeoutException){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:ServerResponseException){
            Log.d(TAG, "getCuratedPhotos: Exception:Server response${e.response.status}")
            null
        }catch (e:ClientRequestException){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:RedirectResponseException){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }catch (e:Exception){
            Log.d(TAG, "getCuratedPhotos: ${e.message}")
            null
        }
    }
}