package com.example.cryptoapp.network

import com.example.cryptoapp.model.CoinDetailModel
import com.example.cryptoapp.model.CoinModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinService {

    @GET("coins")
    suspend fun getCoins():List<CoinModel>

    @GET("coins/{coin_id}")
    suspend fun getCoinDetail(@Path("coin_id") coinId:String):CoinDetailModel

}