package com.example.cryptoexchangeapp.MainFragments.SettingsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentSetPasswordBinding

class SetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentSetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

}