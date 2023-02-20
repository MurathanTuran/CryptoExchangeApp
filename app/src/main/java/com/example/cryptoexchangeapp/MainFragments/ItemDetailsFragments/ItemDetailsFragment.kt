package com.example.cryptoexchangeapp.MainFragments.ItemDetailsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentItemDetailsBinding

class ItemDetailsFragment : Fragment() {

    private lateinit var binding: FragmentItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

}