package com.example.bookshelf.network.service

import com.example.bookshelf.network.models.IpGeoLocationService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class DefaultCountryNetworkService : BaseService<DefaultCountryNetworkService.DefaultCountryApiService>() {
    override fun getService(okHttpClient: OkHttpClient): DefaultCountryApiService {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return Retrofit.Builder()
            .baseUrl("https://www.ip-api.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(DefaultCountryApiService::class.java)
    }

    interface DefaultCountryApiService {

        @GET("json")
        fun getDefaultCountry(): Call<IpGeoLocationService?>

    }

    companion object {

        private var defaultCountryApiService: DefaultCountryApiService? = null
        val defaultCountryApiServices: DefaultCountryApiService
            get() {
                if (defaultCountryApiService == null) {
                    defaultCountryApiService = DefaultCountryNetworkService().newTransaction()
                }
                return defaultCountryApiService!!
            }
    }
}