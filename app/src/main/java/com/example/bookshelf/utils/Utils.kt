package com.example.bookshelf.utils

import android.content.Context
import com.example.bookshelf.database.AppDatabase
import com.example.bookshelf.network.domain.data.BookShelfDataRepository
import com.example.bookshelf.network.domain.local.UserLocalRepositoryImpl
import com.example.bookshelf.network.domain.local.UsersUseCaseImpl
import com.example.bookshelf.network.domain.remote.BookShelfRemoteRepository
import com.example.bookshelf.network.service.BookShelfNetworkService
import com.example.bookshelf.network.service.CountryListNetworkService
import com.example.bookshelf.network.service.DefaultCountryNetworkService
import com.thedeanda.lorem.Lorem
import com.thedeanda.lorem.LoremIpsum

object Utils {

    fun provideBookShelfDataRepository(): BookShelfDataRepository {
        return BookShelfDataRepository.getInstance(
            BookShelfRemoteRepository.getInstance(
                CountryListNetworkService.countryListApiServices,
                DefaultCountryNetworkService.defaultCountryApiServices,
                BookShelfNetworkService.bookShelfApiServices
            )
        )
    }

    fun provideUserCaseImpl(context : Context): UsersUseCaseImpl{
        return UsersUseCaseImpl(UserLocalRepositoryImpl(AppDatabase.getDataBase(context).userDao()))
    }

    fun generateLoremIpsum(): String {
        val loremIpsum: Lorem = LoremIpsum.getInstance()
        return loremIpsum.getParagraphs(1, 2)
    }
}