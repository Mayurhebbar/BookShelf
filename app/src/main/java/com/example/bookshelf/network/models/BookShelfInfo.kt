package com.example.bookshelf.network.models

import com.google.gson.annotations.SerializedName

data class BookShelfInfo(
    @SerializedName("id") val id: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("score") val score: Double? = null,
    @SerializedName("popularity") val popularity: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("publishedChapterDate") val publishedChapterDate: Long? = null
)