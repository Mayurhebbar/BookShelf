package com.example.bookshelf.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object AppCoroutineContextProvider : CoroutineContextProvider {
    override val Main: CoroutineContext by lazy { Dispatchers.Main }
    override val IO: CoroutineContext by lazy { Dispatchers.IO }
}


interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext
}