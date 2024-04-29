package com.example.bookshelf.network.domain.data

import com.example.bookshelf.network.domain.BookShelfRepository
import com.example.bookshelf.network.domain.remote.BookShelfRemoteRepository
import com.example.bookshelf.network.models.BookShelfInfo
import com.example.bookshelf.network.models.CountryData
import com.example.bookshelf.network.models.IpGeoLocationService

class BookShelfDataRepository(private val mBookShelfRemoteRepository: BookShelfRemoteRepository) :
    BookShelfRepository {
    override suspend fun getCountryList(
        callback: BookShelfRepository.Callback<CountryData>
    ) {
        mBookShelfRemoteRepository.getCountriesList(object :
            BookShelfRepository.Callback<CountryData> {
            override fun onSuccess(response: CountryData) {
                callback.onSuccess(response)
            }

            override fun onFailure(error: CountryData?) {
                callback.onFailure(error)
            }

        })
    }

    override suspend fun getDefaultCountry(
        callback: BookShelfRepository.Callback<IpGeoLocationService>
    ) {
        mBookShelfRemoteRepository.getDefaultCountry(object :
            BookShelfRepository.Callback<IpGeoLocationService> {
            override fun onSuccess(response: IpGeoLocationService) {
                callback.onSuccess(response)
            }

            override fun onFailure(error: IpGeoLocationService?) {
                callback.onFailure(error)
            }

        })
    }

    override suspend fun getBookList(
        callback: BookShelfRepository.Callback<List<BookShelfInfo?>?>
    ) {
        mBookShelfRemoteRepository.getBookList(object :
            BookShelfRepository.Callback<List<BookShelfInfo?>?> {
            override fun onSuccess(response: List<BookShelfInfo?>?) {
                callback.onSuccess(response)
            }

            override fun onFailure(error: List<BookShelfInfo?>?) {
                callback.onFailure(error)
            }

        })
    }

    companion object{
        private var instance : BookShelfDataRepository? = null
        private val tag = BookShelfDataRepository::class.java.simpleName

        fun getInstance(bookShelfRemoteRepository: BookShelfRemoteRepository) : BookShelfDataRepository {
            if(instance == null){
                instance = BookShelfDataRepository(bookShelfRemoteRepository)
            }
            return instance!!
        }
    }
}