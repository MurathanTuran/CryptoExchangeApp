package com.example.cryptoexchangeapp.Service

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoexchangeapp.Adapter.RecyclerAdapter
import com.example.cryptoexchangeapp.Model.CryptoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GetData(private var compositeDisposable: CompositeDisposable?, private val recyclerView: RecyclerView, private val fragmentName: String) {

    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var recyclerAdapter: RecyclerAdapter? = null

    fun getData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CryptoApiService::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable -> Toast.makeText(recyclerView.context, throwable.toString(), Toast.LENGTH_LONG).show() }
            .subscribe(this::handleResponseForGetData) {
                Toast.makeText(recyclerView.context, it.toString(), Toast.LENGTH_LONG).show()
            })
    }

    fun getData(name: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CryptoApiService::class.java)

        compositeDisposable?.add(retrofit.getData(name)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable -> Toast.makeText(recyclerView.context, throwable.toString(), Toast.LENGTH_LONG).show() }
            .subscribe(this::handleResponseForGetData) {
                Toast.makeText(recyclerView.context, it.toString(), Toast.LENGTH_LONG).show()
            })
    }

    private fun handleResponseForGetData(cryptoList : List<CryptoModel>?){
        cryptoModels = ArrayList(cryptoList)
        cryptoModels?.let {
            recyclerAdapter = RecyclerAdapter(it, fragmentName)
            recyclerView.adapter = recyclerAdapter
        }
    }

}