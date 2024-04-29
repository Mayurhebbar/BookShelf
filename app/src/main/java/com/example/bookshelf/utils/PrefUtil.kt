package com.example.bookshelf.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.bookshelf.utils.SPConstants.IS_USER_LOGGED_IN
import com.example.bookshelf.utils.SPConstants.USER_ID

class PrefUtil(private val context: Context) {

    private fun getSharedPreference() : SharedPreferences {
        return context.getSharedPreferences(SPConstants.PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isUserCurrentlyLoggedIn() : Boolean{
        return getSharedPreference().getBoolean(IS_USER_LOGGED_IN, false)
    }

    fun saveLastLoginID(lastLoginUserId : Long){
        getSharedPreference().edit().putLong(USER_ID,lastLoginUserId).apply()
    }

    fun saveLastLogin(isUserLoggedIn : Boolean){
        getSharedPreference().edit().putBoolean(IS_USER_LOGGED_IN,isUserLoggedIn).apply()
    }

    fun getUserId() : Long{
        return getSharedPreference().getLong(USER_ID, 0)
    }

    companion object{
        fun getInstance(context: Context) : PrefUtil{
            return PrefUtil(context = context)
        }
    }

}