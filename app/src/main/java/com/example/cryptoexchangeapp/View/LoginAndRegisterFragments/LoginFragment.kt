package com.example.cryptoexchangeapp.View.LoginAndRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.cryptoexchangeapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonClick()
    }

    private fun buttonClick(){
        binding.loginButtonLoginFragment.setOnClickListener {
            val email = binding.emailTextLoginFragment.text.toString()
            val password = binding.passwordTextLoginFragment.text.toString()
            if(checkText(email) && checkText(password)){
                loginUser(email, password)
            }
        }
    }

    private fun checkText(string: String): Boolean {
        if(string.equals("")){
            Toast.makeText(requireContext(), "Fill areas", Toast.LENGTH_LONG).show()
            return false
        }
        else{
            return true
        }
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Navigation.findNavController(requireView()).navigate(LoginFragmentDirections.actionLoginFragmentToContainerFragment())
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

}