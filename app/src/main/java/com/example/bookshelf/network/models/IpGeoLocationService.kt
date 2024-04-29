package com.example.bookshelf.network.models

import com.google.gson.annotations.SerializedName

data class IpGeoLocationService(
    @SerializedName("country")val country: String? = null,
    @SerializedName("countryCode")val countryCode: String? = null
)
