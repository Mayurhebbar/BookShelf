package com.example.bookshelf.network.domain

import com.example.bookshelf.network.models.BookShelfInfo
import com.example.bookshelf.network.models.CountryData
import com.example.bookshelf.network.models.IpGeoLocationService


interface BookShelfRepository {

    suspend fun getCountryList(callback: BookShelfRepository.Callback<CountryData>)

    suspend fun getDefaultCountry(callback: BookShelfRepository.Callback<IpGeoLocationService>)
    suspend fun getBookList(
        callback: BookShelfRepository.Callback<List<BookShelfInfo?>?>
    )

    interface Callback<T> {

        fun onSuccess(response: T)

        fun onFailure(error: T?)

    }
}