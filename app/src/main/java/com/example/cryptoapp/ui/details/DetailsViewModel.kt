package com.example.cryptoapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.model.CoinDetailModel
import com.example.cryptoapp.model.CoinModel
import com.example.cryptoapp.repository.CoinRepository
import com.example.cryptoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val coinRepository: CoinRepository): ViewModel() {


    private val _coinDetail = MutableLiveData<Resource<CoinDetailModel>>()
    val coinDetail : LiveData<Resource<CoinDetailModel>> = _coinDetail

    fun fetchCoinDetail(coinId:String) {

        viewModelScope.launch {
            coinRepository
                .getCoinDetail(coinId)
                .onStart {
                    _coinDetail.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _coinDetail.postValue(Resource.Error(it))
                    }
                }
                .collect { data->
                    _coinDetail.postValue(Resource.Success(data))
                }
        }
    }
}