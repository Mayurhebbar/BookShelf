package com.example.bookshelf.network.domain.local

import com.example.bookshelf.database.Users


interface UsersUseCase {
    suspend fun addUser(users: Users): Long
    suspend fun getUserLoginVerify(mobNum: String, password: String): Users
    suspend fun getUserData(id:Long): Users

}