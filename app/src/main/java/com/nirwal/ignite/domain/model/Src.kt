package com.nirwal.ignite.domain.model

import com.google.gson.annotations.SerializedName


data class Src (

    @SerializedName("original"  ) var original  : String? = null,
    @SerializedName("large2x"   ) var large2x   : String? = null,
    @SerializedName("large"     ) var large     : String? = null,
    @SerializedName("medium"    ) var medium    : String? = null,
    @SerializedName("small"     ) var small     : String? = null,
    @SerializedName("portrait"  ) var portrait  : String? = null,
    @SerializedName("landscape" ) var landscape : String? = null,
    @SerializedName("tiny"      ) var tiny      : String? = null

)