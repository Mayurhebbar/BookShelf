package com.example.bookshelf.network.domain.local

import com.example.bookshelf.database.Users
import com.example.bookshelf.database.UsersDao

class UserLocalRepositoryImpl(
    private  var usersDao: UsersDao

):UserLocalRepository{

    override fun addUser(users:Users): Long {
        return usersDao.insertOrUpdateUser(users)
    }

    override fun verifyLoginUser(email:String,password:String): Users {
        return usersDao.readLoginData(email = email,password = password )
    }

    override fun getUserDataDetails(id:Long): Users {
        return usersDao.getUserDataDetails(id)
    }




}