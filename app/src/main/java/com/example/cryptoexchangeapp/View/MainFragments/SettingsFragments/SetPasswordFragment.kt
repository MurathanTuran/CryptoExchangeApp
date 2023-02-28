package com.example.cryptoexchangeapp.View.MainFragments.SettingsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.View.MainFragments.SettingsFragment
import com.example.cryptoexchangeapp.databinding.FragmentSetPasswordBinding
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentSetPasswordBinding

    private var auth = Firebase.auth
    private var email = auth.currentUser!!.email

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPassword()
    }

    private fun setPassword(){
        binding.setPasswordButtonSetPasswordFragment.setOnClickListener {
            val oldPassword = binding.oldPasswordTextSetPasswordFragment.text.toString()
            val confirmPassword = binding.confirmPasswordTextSetPasswordFragment.text.toString()
            val newPassword = binding.newPasswordTextSetPasswordFragment.text.toString()
            if(!newPassword.equals("") && !confirmPassword.equals("") && !oldPassword.equals("")){
                if(oldPassword.equals(confirmPassword)){
                    val currentUser = auth.currentUser
                    val credential = EmailAuthProvider.getCredential(currentUser!!.email.toString(), binding.oldPasswordTextSetPasswordFragment.text.toString())
                    currentUser.reauthenticate(credential).addOnSuccessListener {
                        currentUser.updatePassword(binding.newPasswordTextSetPasswordFragment.text.toString()).addOnSuccessListener {
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.frameLayout,SettingsFragment())
                            transaction.commit()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(requireContext().applicationContext, "The confirm password is not equal to old password", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(requireContext().applicationContext, "Fill areas", Toast.LENGTH_LONG).show()
            }
        }

    }

}