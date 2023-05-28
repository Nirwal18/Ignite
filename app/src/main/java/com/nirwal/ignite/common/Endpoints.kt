package com.nirwal.ignite.common

object Endpoints {
    private const val BASE_URL = "https://api.pexels.com/v1"
    const val SEARCH_URL = "$BASE_URL/search"
    const val curatedPhotos = "$BASE_URL/curated"
    const val GET_PHOTO_BY_ID =  "${BASE_URL}/photos/"
}