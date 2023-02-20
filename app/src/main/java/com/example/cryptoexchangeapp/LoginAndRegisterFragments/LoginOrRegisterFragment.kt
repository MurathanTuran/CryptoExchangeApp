package com.example.cryptoexchangeapp.LoginAndRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.cryptoexchangeapp.databinding.FragmentLoginOrRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonClick()
    }

    private fun buttonClick(){
        binding.loginButtonLoginOrRegisterFragment.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(LoginOrRegisterFragmentDirections.actionLoginOrRegisterFragmentToLoginFragment())
        }

        binding.registerButtonLoginOrRegisterFragment.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(LoginOrRegisterFragmentDirections.actionLoginOrRegisterFragmentToRegisterFragment())
        }
    }

}