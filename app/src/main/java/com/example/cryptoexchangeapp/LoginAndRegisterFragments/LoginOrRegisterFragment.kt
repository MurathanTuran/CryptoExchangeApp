package com.example.cryptoexchangeapp.LoginAndRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.databinding.FragmentLoginOrRegisterBinding

class LoginOrRegisterFragment : Fragment() {

    private lateinit var binding: FragmentLoginOrRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginOrRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


}