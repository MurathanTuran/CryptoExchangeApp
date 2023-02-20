package com.example.cryptoexchangeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.cryptoexchangeapp.LoginAndRegisterFragments.LoginOrRegisterFragmentDirections
import com.example.cryptoexchangeapp.MainFragments.ContainerFragment
import com.example.cryptoexchangeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        checkCurrentUser()
    }

    private fun checkCurrentUser(){
        if(auth.currentUser!=null){
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,ContainerFragment())
            transaction.commit()
        }
    }

}