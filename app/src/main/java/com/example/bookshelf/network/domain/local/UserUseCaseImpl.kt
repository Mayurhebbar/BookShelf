package com.example.bookshelf.network.domain.local

import com.example.bookshelf.database.Users


class UsersUseCaseImpl(private var userLocalRepositoryImpl: UserLocalRepositoryImpl):UsersUseCase{
    override suspend fun addUser(users: Users): Long {
        return userLocalRepositoryImpl.addUser(users)
    }

    override suspend fun getUserLoginVerify(mobNum:String, password:String): Users {
        return userLocalRepositoryImpl.verifyLoginUser(mobNum, password)
    }

    override suspend fun getUserData(id: Long): Users {
        return userLocalRepositoryImpl.getUserDataDetails(id)
    }

}