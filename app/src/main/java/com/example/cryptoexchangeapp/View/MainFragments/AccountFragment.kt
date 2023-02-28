package com.example.cryptoexchangeapp.View.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoexchangeapp.Service.GetData
import com.example.cryptoexchangeapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.disposables.CompositeDisposable

class AccountFragment : Fragment(){

    private lateinit var binding: FragmentAccountBinding

    private var auth = Firebase.auth
    private var firestore = Firebase.firestore
    private var email = auth.currentUser!!.email

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAccountFragment.layoutManager = layoutManager

        getStarredData()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    private fun getStarredData() {
        firestore.collection("Users").whereEqualTo("email", email)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show()
                } else {
                    if (value != null) {
                        val documentId = value.documents[0].id
                        firestore.collection("Users").document(documentId).get()
                            .addOnSuccessListener {
                        val starredArray = it.get("starred") as ArrayList<Any>
                        val ids: String
                        if(starredArray.size != 0){
                            ids = starredArray.joinToString(separator = ",")
                        }
                        else{
                            ids = "null"
                        }
                        GetData(compositeDisposable, binding.recyclerViewAccountFragment, "AccountFragment").getData(ids)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}