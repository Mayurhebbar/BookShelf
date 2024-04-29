package com.example.bookshelf.network.service

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

abstract class BaseService<T> {
    fun newTransaction(): T {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .build()
            .newBuilder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        return getService(okHttpClientBuilder.build())
    }

    protected abstract fun getService(okHttpClient: OkHttpClient): T
}