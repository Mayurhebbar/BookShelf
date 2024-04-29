package com.example.bookshelf.livedata

import androidx.annotation.StringDef

class Result<T> private constructor(@Status val status: String, val data: T?, val error: String?) {

    companion object {
        @Retention(AnnotationRetention.SOURCE) @StringDef(SUCCESS, ERROR, LOADING)
        annotation class Status

        const val SUCCESS = "success"
        const val ERROR = "error"
        const val LOADING = "loading"

        fun <T> success(data: T?): Result<T> {
            return Result(SUCCESS, data, null)
        }

        fun <T> error(error: String?): Result<T> {
            return Result(ERROR, null, error)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(LOADING, data, null)
        }
    }
}