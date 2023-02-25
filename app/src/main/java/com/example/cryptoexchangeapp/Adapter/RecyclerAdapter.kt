package com.example.cryptoexchangeapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoexchangeapp.Model.CryptoModel
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.Service.SetDesignColor
import com.example.cryptoexchangeapp.databinding.RecyclerRowBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val cryptoModel: ArrayList<CryptoModel>, private val fragmentName: String): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var auth = Firebase.auth
    private var firestore = Firebase.firestore
    private var email = auth.currentUser!!.email

    class ViewHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position%2==0){
            SetDesignColor(holder, fragmentName).blackBg()
        }
        else{
            SetDesignColor(holder, fragmentName).whiteBg()
        }

        Picasso.get().load(cryptoModel[position].image).resize(75, 75).into(holder.binding.symbolImageRecyclerRow)
        holder.binding.cryptoSymbolTextRecyclerRow.text = cryptoModel[position].symbol.uppercase()
        holder.binding.cryptoPriceTextRecyclerRow.text = cryptoModel[position].current_price.toString()
        holder.binding.cryptoIdTextRecyclerRow.text = cryptoModel[position].id

        starClicked(holder, position)

    }

    override fun getItemCount(): Int {
        return cryptoModel.size
    }

    private fun starClicked(holder: ViewHolder, position: Int) {
        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(holder.itemView.context, error.toString(), Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    val documentId = value.documents[0].id
                    firestore.collection("Users").document(documentId).get().addOnSuccessListener {
                        val starredArray = it.get("starred") as ArrayList<Any>
                        holder.binding.starImageRecyclerRow.setOnClickListener {
                            val clickedId = holder.binding.cryptoIdTextRecyclerRow.text.toString()
                            if(starredArray.contains(clickedId)){
                                starredArray.remove(clickedId)
                                updateStarred(holder, position, documentId, starredArray, "remove")
                            }
                            else{
                                starredArray.add(clickedId)
                                updateStarred(holder, position, documentId, starredArray, "add")
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateStarred(holder: ViewHolder, position: Int, documentId: String, starredArray: ArrayList<Any>, action: String){
        firestore.collection("Users").document(documentId).update("starred", starredArray).addOnSuccessListener {
            println(holder.binding.cryptoIdTextRecyclerRow.text.toString())
            if(action.equals("add")){
                holder.binding.starImageRecyclerRow.setImageResource(R.drawable.icon_starred)
            }
            else if(action.equals("remove")){
                if(position%2==0){
                    holder.binding.starImageRecyclerRow.setImageResource(R.drawable.icon_white_star)
                }
                else{
                    holder.binding.starImageRecyclerRow.setImageResource(R.drawable.icon_black_star)
                }
            }
            else{
                println("Wrong action")
            }
        }.addOnFailureListener {
            Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


}