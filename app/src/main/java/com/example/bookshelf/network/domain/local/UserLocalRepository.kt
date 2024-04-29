package com.example.bookshelf.network.domain.local

import com.example.bookshelf.database.Users


interface UserLocalRepository {

    fun addUser(users: Users):Long

    fun verifyLoginUser(mobNum:String,password:String): Users

    fun getUserDataDetails(id:Long):Users

}