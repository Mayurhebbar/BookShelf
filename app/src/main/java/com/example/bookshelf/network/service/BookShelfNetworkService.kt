package com.example.bookshelf.network.service

import com.example.bookshelf.network.models.BookShelfInfo
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BookShelfNetworkService : BaseService<BookShelfNetworkService.BookShelfApiService>() {
    override fun getService(okHttpClient: OkHttpClient): BookShelfApiService {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        return Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(BookShelfApiService::class.java)
    }

    interface BookShelfApiService {

        @GET("b/CNGI")
        fun getBookList(): Call<List<BookShelfInfo?>?>

    }

    companion object {

        private var bookShelfApiService: BookShelfApiService? = null
        val bookShelfApiServices: BookShelfApiService
            get() {
                if (bookShelfApiService == null) {
                    bookShelfApiService = BookShelfNetworkService().newTransaction()
                }
                return bookShelfApiService!!
            }
    }
}