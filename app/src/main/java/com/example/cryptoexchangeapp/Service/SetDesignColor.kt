package com.example.cryptoexchangeapp.Service

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptoexchangeapp.Adapter.RecyclerAdapter
import com.example.cryptoexchangeapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SetDesignColor(private val holder: RecyclerAdapter.ViewHolder, private val fragmentName: String) {

    private var auth = Firebase.auth
    private var firestore = Firebase.firestore
    private var email = auth.currentUser!!.email

    fun blackBg(){
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        holder.binding.cryptoSymbolTextRecyclerRow.setTextColor(Color.parseColor("#FFFFFF"))
        holder.binding.cryptoAmountTextRecyclerRow.setTextColor(Color.parseColor("#FFFFFF"))
        holder.binding.relativeLayoutBought.visibility = View.GONE
        holder.binding.relativeLayoutSold.visibility = View.GONE

        setDropDownItemsColor(Color.parseColor("#000000"), Color.parseColor("#FFFFFF"))

        setStarColor(R.drawable.icon_white_star)
    }

    fun whiteBg(){
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        holder.binding.cryptoSymbolTextRecyclerRow.setTextColor(Color.parseColor("#000000"))
        holder.binding.cryptoAmountTextRecyclerRow.setTextColor(Color.parseColor("#000000"))
        holder.binding.relativeLayoutBought.visibility = View.GONE
        holder.binding.relativeLayoutSold.visibility = View.GONE

        setDropDownItemsColor(Color.parseColor("#FFFFFF"), Color.parseColor("#000000"))

        setStarColor(R.drawable.icon_black_star)
    }

    private fun setStarColor(resId: Int){
        if(fragmentName.equals("MainFragment")){
            holder.binding.starImageRecyclerRow.visibility = View.VISIBLE
            holder.binding.starImageRecyclerRow.setImageResource(resId)
            isStarred()
        }
        else if(fragmentName.equals("AccountFragment")){
            holder.binding.starImageRecyclerRow.visibility = View.VISIBLE
            holder.binding.starImageRecyclerRow.setImageResource(R.drawable.icon_starred)
        }
        else if(fragmentName.equals("WalletFragment")){
            holder.binding.starImageRecyclerRow.visibility = View.GONE
            holder.binding.cryptoAmountTextRecyclerRow.visibility = View.VISIBLE
            holder.binding.cryptoTotalTextRecyclerRow.visibility = View.VISIBLE

            setWalletData()
        }
        else{
            println("Wrong fragment name")
        }
    }

    private fun isStarred() {
        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(holder.itemView.context, error.toString(), Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    val documentId = value.documents[0].id
                    firestore.collection("Users").document(documentId).get().addOnSuccessListener {
                        for(starredId in it.get("starred") as ArrayList<Any>){
                            if(starredId.toString().equals(holder.binding.cryptoIdTextRecyclerRow.text)){
                                holder.binding.starImageRecyclerRow.setImageResource(R.drawable.icon_starred)
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setDropDownItemsColor(buttonColor: Int, ediTextColor: Int){
        holder.binding.boughtAmountTextRecyclerRow.setBackgroundColor(ediTextColor)
        holder.binding.boughtAmountTextRecyclerRow.setTextColor(buttonColor)
        holder.binding.boughtAmountTextRecyclerRow.setHintTextColor(Color.parseColor("#CCCCCC"))

        holder.binding.soldAmountTextRecyclerRow.setBackgroundColor(ediTextColor)
        holder.binding.soldAmountTextRecyclerRow.setTextColor(buttonColor)
        holder.binding.soldAmountTextRecyclerRow.setHintTextColor(Color.parseColor("#CCCCCC"))

        holder.binding.boughtButtonRecyclerRow.setBackgroundColor(buttonColor)
        holder.binding.boughtButtonRecyclerRow.setTextColor(ediTextColor)

        holder.binding.soldButtonRecyclerRow.setBackgroundColor(buttonColor)
        holder.binding.soldButtonRecyclerRow.setTextColor(ediTextColor)
    }

    private fun setWalletData() {
        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(holder.itemView.context, error.toString(), Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    val documentId = value.documents[0].id
                    firestore.collection("Users").document(documentId).get().addOnSuccessListener {
                        val wallet = it.get("wallet") as HashMap<String, Float>
                        val cryptoId = holder.binding.cryptoIdTextRecyclerRow.text.toString()

                        val coins = wallet.get(cryptoId).toString().toFloat()
                        val price = holder.binding.cryptoPriceTextRecyclerRow.text.toString()
                        val amount = (coins * price.toFloat()).toString()

                        holder.binding.cryptoAmountTextRecyclerRow.text = coins.toString()
                        holder.binding.cryptoTotalTextRecyclerRow.text = amount
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}