package com.example.cryptoexchangeapp.View.LoginAndRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.cryptoexchangeapp.Model.CryptoModel
import com.example.cryptoexchangeapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth
    private var firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         buttonClick()
    }

    private fun buttonClick(){
        binding.registerButtonRegisterFragment.setOnClickListener {
            val email = binding.emailTextRegisterFragment.text.toString()
            val password = binding.passwordTextRegisterFragment.text.toString()
            if(checkText(email) && checkText(password)){
                createUser(email, password)
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

    private fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val total = 0
            val wallet = HashMap<String, Long>()
            val starred = ArrayList<CryptoModel>()

            createCollection(email, total, wallet, starred)
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun createCollection(email: String, total: Int, wallet: HashMap<String, Long>, starred: ArrayList<CryptoModel>){
        val userCollection = HashMap<String, Any>()
        userCollection.put("email", email)
        userCollection.put("total", total)
        userCollection.put("wallet", wallet)
        userCollection.put("starred", starred)

        firestore.collection("Users").add(userCollection).addOnSuccessListener {
            Navigation.findNavController(requireView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToContainerFragment())
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

}