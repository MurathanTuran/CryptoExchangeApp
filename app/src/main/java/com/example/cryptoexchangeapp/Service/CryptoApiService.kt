package com.example.cryptoexchangeapp.Service

import io.reactivex.Observable
import com.example.cryptoexchangeapp.Model.CryptoModel
import retrofit2.http.GET

interface CryptoApiService {

    @GET("api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    fun getData(): Observable<List<CryptoModel>>

}