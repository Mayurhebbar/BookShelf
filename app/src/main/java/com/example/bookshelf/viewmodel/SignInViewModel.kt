package com.example.bookshelf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.database.Users
import com.example.bookshelf.livedata.Result
import com.example.bookshelf.network.domain.local.UsersUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private var usersUseCaseImpl: UsersUseCaseImpl) : ViewModel() {
    private val _getUserLoginDataStatus = MutableLiveData<Result<Users>>()

    val getUserLoginDataStatus: LiveData<Result<Users>> = _getUserLoginDataStatus

    private val _getUserProfileDataStatus = MutableLiveData<Result<Users>>()

    val getUserProfileDataStatus: LiveData<Result<Users>> = _getUserProfileDataStatus

    fun getUserLoginDataStatus(email:String, password:String) {
        viewModelScope.launch(Dispatchers.IO){
            _getUserLoginDataStatus.postValue(Result.loading())
            try {
                val data = usersUseCaseImpl.getUserLoginVerify(email,password)
                _getUserLoginDataStatus.postValue(Result.success(data))
            } catch (exception: Exception) {
                _getUserLoginDataStatus.postValue(Result.error(exception.message))
            }
        }
    }

    fun getUserProfileData(id:Long){
        viewModelScope.launch(Dispatchers.IO){
            _getUserProfileDataStatus.postValue(Result.loading())
            try {
                val data = usersUseCaseImpl.getUserData(id)
                _getUserProfileDataStatus.postValue(Result.success(data))
            } catch (exception: Exception) {
                _getUserProfileDataStatus.postValue(Result.error(exception.message))
            }
        }
    }


}