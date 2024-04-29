package com.example.bookshelf.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(contextProvider: CoroutineContextProvider) : ViewModel(),
    CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = contextProvider.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}