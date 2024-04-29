package com.example.bookshelf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookshelf.network.domain.BookShelfRepository
import com.example.bookshelf.network.domain.data.BookShelfDataRepository
import com.example.bookshelf.network.models.BookShelfInfo
import com.example.bookshelf.network.models.CountryData
import com.example.bookshelf.network.models.IpGeoLocationService
import kotlinx.coroutines.launch

class BookShelfViewModel(private val bookShelfDataRepository: BookShelfDataRepository,
                         private val contextProvider: CoroutineContextProvider = AppCoroutineContextProvider) :
    BaseViewModel(contextProvider) {

    private val _countryListSuccessData = MutableLiveData<CountryData>()
    val countryListSuccessData: LiveData<CountryData> = _countryListSuccessData

    private val _countryListFailureData = MutableLiveData<CountryData?>()
    val countryListFailureData: LiveData<CountryData?> = _countryListFailureData

    private val _defaultCountrySuccessData = MutableLiveData<IpGeoLocationService>()
    val defaultCountrySuccessData: LiveData<IpGeoLocationService> = _defaultCountrySuccessData

    private val _defaultCountryFailureData = MutableLiveData<IpGeoLocationService?>()
    val defaultCountryFailureData: LiveData<IpGeoLocationService?> = _defaultCountryFailureData

    private val _bookListSuccessData = MutableLiveData<List<BookShelfInfo?>?>()
    val bookListSuccessData: LiveData<List<BookShelfInfo?>?> = _bookListSuccessData

    private val _bookListFailureData = MutableLiveData<List<BookShelfInfo?>?>()
    val bookListFailureData: LiveData<List<BookShelfInfo?>?> = _bookListFailureData

    fun fetchCountryList() {
        launch(contextProvider.IO) {
            bookShelfDataRepository.getCountryList(
                object : BookShelfRepository.Callback<CountryData> {
                    override fun onSuccess(response: CountryData) {
                        _countryListSuccessData.postValue(response)
                    }

                    override fun onFailure(error: CountryData?) {
                        _countryListFailureData.postValue(error)
                    }
                }
            )
        }

    }

    fun getDefaultCountry() {
        launch(contextProvider.IO) {
            bookShelfDataRepository.getDefaultCountry(
                object : BookShelfRepository.Callback<IpGeoLocationService> {
                    override fun onSuccess(response: IpGeoLocationService) {
                        _defaultCountrySuccessData.postValue(response)
                    }

                    override fun onFailure(error: IpGeoLocationService?) {
                        _defaultCountryFailureData.postValue(error)
                    }
                }
            )
        }

    }

    fun getBookList() {
        launch(contextProvider.IO) {
            bookShelfDataRepository.getBookList(
                object : BookShelfRepository.Callback<List<BookShelfInfo?>?> {
                    override fun onSuccess(response: List<BookShelfInfo?>?) {
                        _bookListSuccessData.postValue(response)
                    }

                    override fun onFailure(error: List<BookShelfInfo?>?) {
                        _bookListFailureData.postValue(error)
                    }
                }
            )
        }

    }
}