package com.example.bookshelf.network.models

import com.google.gson.annotations.SerializedName

data class CountryData(
    @SerializedName("status") val status: String,
    @SerializedName("status-code") val statusCode: Int,
    @SerializedName("version") val version: String,
    @SerializedName("access") val access: String,
    @SerializedName("data") val data: Map<String, Country>
)

data class Country(
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String
)
