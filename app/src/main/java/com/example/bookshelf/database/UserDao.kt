package com.example.bookshelf.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

const val dbUserTable = "UserTable"

@Dao
interface UsersDao {

    //for single user insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(users: Users): Long

    //checking user exist or not in our db
    @Query("SELECT * FROM $dbUserTable WHERE email = :email AND password  = :password")
    fun readLoginData(email: String, password: String):Users


    //getting user data details
    @Query("select * from $dbUserTable where id Like :id")
    fun getUserDataDetails(id:Long):Users



}