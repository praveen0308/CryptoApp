package com.example.cryptoapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.model.CoinModel
import com.example.cryptoapp.repository.CoinRepository
import com.example.cryptoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val coinRepository: CoinRepository) : ViewModel() {


    private val _coinList = MutableLiveData<Resource<List<CoinModel>>>()
    val coinList: LiveData<Resource<List<CoinModel>>> = _coinList

    fun fetchCoins() {

        viewModelScope.launch {
            coinRepository
                .getCoins()
                .onStart {
                    _coinList.postValue(Resource.Loading(true))
                }
                .catch { exception ->
                    exception.message?.let {
                        _coinList.postValue(Resource.Error(it))
                    }
                }
                .collect { items ->
                    _coinList.postValue(Resource.Success(items))
                }
        }
    }


}