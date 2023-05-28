package com.nirwal.ignite.domain.model

import com.google.gson.annotations.SerializedName
import com.nirwal.ignite.domain.database.PhotoEntity


data class Photo (

    @SerializedName("id"               ) var id              : Int?     = null,
    @SerializedName("width"            ) var width           : Int?     = null,
    @SerializedName("height"           ) var height          : Int?     = null,
    @SerializedName("url"              ) var url             : String?  = null,
    @SerializedName("photographer"     ) var photographer    : String?  = null,
    @SerializedName("photographer_url" ) var photographerUrl : String?  = null,
    @SerializedName("photographer_id"  ) var photographerId  : Int?     = null,
    @SerializedName("avg_color"        ) var avgColor        : String?  = null,
    @SerializedName("src"              ) var src             : Src?     = Src(),
    @SerializedName("liked"            ) var liked           : Boolean? = null,
    @SerializedName("alt"              ) var alt             : String?  = null

){
    fun toPhotoEntity():PhotoEntity{
        return PhotoEntity(
            remoteId = id ?: 0,
            alt=alt,
            photographer = photographer,
            photographerId = photographerId,
            originalUrl = src?.original,
            smallUrl = src?.small,
            largeUrl = src?.large,
            mediumUrl = src?.medium,
        )
    }

}