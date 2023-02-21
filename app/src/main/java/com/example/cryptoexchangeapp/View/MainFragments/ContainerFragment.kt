package com.example.cryptoexchangeapp.View.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.Model.CryptoModel
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.Service.CryptoApiService
import com.example.cryptoexchangeapp.databinding.FragmentContainerBinding
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.android.schedulers.AndroidSchedulers

class ContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerBinding

    private var compositeDisposable: CompositeDisposable? = null
    private var cryptoModels: ArrayList<CryptoModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFragment(MainFragment())
        bottomNavigationClick()
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
    }

    private fun bottomNavigationClick(){
        val bottomNav = binding.bottomNavigationViewContainerFragment

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main -> {loadFragment(MainFragment())}
                R.id.account -> {loadFragment(AccountFragment())}
                R.id.wallet -> {loadFragment(WalletFragment())}
                R.id.settings -> {loadFragment(SettingsFragment())}
            }
            true
        }
    }

    private fun getData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CryptoApiService::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(cryptoList : List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)
        for (cryptoModel: CryptoModel in cryptoModels!!){
            println(cryptoModel.name)
            println(cryptoModel.symbol)
            println(cryptoModel.current_price)
            println(cryptoModel.image)
            println("///////////////////////")
        }
    }



}