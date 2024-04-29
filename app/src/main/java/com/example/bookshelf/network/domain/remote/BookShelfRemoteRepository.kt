package com.example.bookshelf.network.domain.remote

import android.util.Log
import com.example.bookshelf.network.domain.BookShelfRepository
import com.example.bookshelf.network.models.BookShelfInfo
import com.example.bookshelf.network.models.CountryData
import com.example.bookshelf.network.models.IpGeoLocationService
import com.example.bookshelf.network.service.BookShelfNetworkService
import com.example.bookshelf.network.service.CountryListNetworkService
import com.example.bookshelf.network.service.DefaultCountryNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookShelfRemoteRepository(
    private val countryListApiService: CountryListNetworkService.CountryListApiService,
    private val defaultCountryApiService: DefaultCountryNetworkService.DefaultCountryApiService,
    private val bookShelfApi: BookShelfNetworkService.BookShelfApiService
) {

    fun getCountriesList(
        callback: BookShelfRepository.Callback<CountryData>
    ){
        val call = countryListApiService.fetchCountries()
        call.enqueue(object : Callback<CountryData?> {
            override fun onResponse(call: Call<CountryData?>,
                                    response: Response<CountryData?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG,"Success API getCountriesList ${response.body()}")
                    callback.onSuccess(response.body()!!)
                } else {
                    Log.d(TAG,"Failure API getCountriesList ${response.message()}")
                    callback.onFailure(null)
                }
            }

            override fun onFailure(call: Call<CountryData?>, t: Throwable) {
                Log.e(TAG,"Failure API getCountriesList $t", t)
                callback.onFailure(null)
            }
        })
    }

    fun getDefaultCountry(
        callback: BookShelfRepository.Callback<IpGeoLocationService>
    ){
        val call = defaultCountryApiService.getDefaultCountry()
        call.enqueue(object : Callback<IpGeoLocationService?> {
            override fun onResponse(call: Call<IpGeoLocationService?>,
                                    response: Response<IpGeoLocationService?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                    Log.d(TAG,"Success API getDefaultCountry ${response.body()}")
                } else {
                    Log.d(TAG,"Failure API getDefaultCountry ${response.message()}")
                    callback.onFailure(null)
                }
            }

            override fun onFailure(call: Call<IpGeoLocationService?>, t: Throwable) {
                Log.e(TAG,"Failure API getDefaultCountry $t", t)
                callback.onFailure(null)
            }
        })
    }

    fun getBookList(
        callback: BookShelfRepository.Callback<List<BookShelfInfo?>?>
    ){
        val call = bookShelfApi.getBookList()
        call.enqueue(object : Callback<List<BookShelfInfo?>?> {
            override fun onResponse(call: Call<List<BookShelfInfo?>?>,
                                    response: Response<List<BookShelfInfo?>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG,"Success API getBookList ${response.body()}")
                    callback.onSuccess(response.body()!!)
                } else {
                    Log.d(TAG,"Failure API getBookList ${response.message()}")
                    callback.onFailure(null)
                }
            }

            override fun onFailure(call: Call<List<BookShelfInfo?>?>, t: Throwable) {
                Log.e(TAG,"Failure API getBookList $t", t)
                callback.onFailure(null)
            }
        })
    }

    companion object{

        private var instance : BookShelfRemoteRepository? = null
        private val TAG = BookShelfRemoteRepository::class.java.simpleName

        fun getInstance(countryListApi: CountryListNetworkService.CountryListApiService,
                        defaultCountryApi: DefaultCountryNetworkService.DefaultCountryApiService,
                        bookShelfApi: BookShelfNetworkService.BookShelfApiService) : BookShelfRemoteRepository {
            if(instance == null){
                instance = BookShelfRemoteRepository(countryListApi, defaultCountryApi, bookShelfApi)
            }
            return instance!!
        }
    }
}