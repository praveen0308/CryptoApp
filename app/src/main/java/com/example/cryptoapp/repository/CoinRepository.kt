package com.example.cryptoapp.repository

import com.example.cryptoapp.model.CoinDetailModel
import com.example.cryptoapp.model.CoinModel
import com.example.cryptoapp.network.CoinService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoinRepository @Inject constructor(private val coinService: CoinService) {


   suspend fun getCoins():Flow<List<CoinModel>> = flow {
        val response = coinService.getCoins()
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun getCoinDetail(coinId:String):Flow<CoinDetailModel> = flow {
        val response = coinService.getCoinDetail(coinId)
        emit(response)
    }.flowOn(Dispatchers.IO)
}