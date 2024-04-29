package com.example.bookshelf.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.database.Users
import com.example.bookshelf.livedata.Result
import com.example.bookshelf.network.domain.local.UsersUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel(private var usersUseCaseImpl: UsersUseCaseImpl) : ViewModel() {

    private val _insertUsersDataStatus = MutableLiveData<Result<Long>>()

    val insertUsersDataStatus: LiveData<Result<Long>> = _insertUsersDataStatus

    private var emailPatterns =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun insertUserData(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            _insertUsersDataStatus.postValue(Result.loading())
            try {
                val data = usersUseCaseImpl.addUser(users)
                _insertUsersDataStatus.postValue(Result.success(data))
            } catch (exception: Exception) {
                _insertUsersDataStatus.postValue(Result.error(exception.message))
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) true else emailPatterns.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*()])(?=.*[a-z])(?=.*[A-Z]).{8,}$")
        return regex.matches(password)
    }

}