package com.nirwal.ignite.domain.model

import com.google.gson.annotations.SerializedName


data class PhotoResult (

    @SerializedName("page"      ) var page     : Int?              = null,
    @SerializedName("per_page"  ) var perPage  : Int?              = null,
    @SerializedName("photos"    ) var photos   : List<Photo>       = listOf(),
    @SerializedName("user_name" ) var userName : String?           = null,
    @SerializedName("email"     ) var email    : String?           = null,
    @SerializedName("name"      ) var name     : String?           = null

)