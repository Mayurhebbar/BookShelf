package com.example.bookshelf.network.service

import com.example.bookshelf.network.models.CountryData
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class CountryListNetworkService : BaseService<CountryListNetworkService.CountryListApiService>() {
    override fun getService(okHttpClient: OkHttpClient): CountryListApiService {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return Retrofit.Builder()
            .baseUrl("https://api.first.org/data/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(CountryListApiService::class.java)
    }

    interface CountryListApiService {

        @GET("v1/countries")
        fun fetchCountries(): Call<CountryData?>

    }

    companion object {

        private var countryListApiService: CountryListApiService? = null
        val countryListApiServices: CountryListApiService
            get() {
                if (countryListApiService == null) {
                    countryListApiService = CountryListNetworkService().newTransaction()
                }
                return countryListApiService!!
            }
    }
}