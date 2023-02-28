package com.example.cryptoexchangeapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        itemClicked(holder)

        boughtClicked(holder)

        soldClicked(holder)
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

    private fun itemClicked(holder: ViewHolder){
        var controller: Boolean? = null

        if(fragmentName.equals("MainFragment") || fragmentName.equals("AccountFragment")){
            holder.itemView.setOnClickListener {
                if(controller==null){
                    holder.binding.starImageRecyclerRow.visibility = View.GONE
                    holder.binding.cryptoAmountTextRecyclerRow.visibility = View.VISIBLE
                    holder.binding.cryptoTotalTextRecyclerRow.visibility = View.VISIBLE

                    holder.binding.relativeLayoutBought.visibility = View.VISIBLE
                    holder.binding.relativeLayoutSold.visibility = View.VISIBLE
                    controller = true
                }
                else if(controller as Boolean){
                    holder.binding.starImageRecyclerRow.visibility = View.VISIBLE
                    holder.binding.cryptoAmountTextRecyclerRow.visibility = View.GONE
                    holder.binding.cryptoTotalTextRecyclerRow.visibility = View.GONE

                    holder.binding.boughtAmountTextRecyclerRow.text = null
                    holder.binding.soldAmountTextRecyclerRow.text = null

                    holder.binding.relativeLayoutBought.visibility = View.GONE
                    holder.binding.relativeLayoutSold.visibility = View.GONE
                    controller = false
                }
                else{
                    holder.binding.starImageRecyclerRow.visibility = View.GONE
                    holder.binding.cryptoAmountTextRecyclerRow.visibility = View.VISIBLE
                    holder.binding.cryptoTotalTextRecyclerRow.visibility = View.VISIBLE

                    holder.binding.relativeLayoutBought.visibility = View.VISIBLE
                    holder.binding.relativeLayoutSold.visibility = View.VISIBLE
                    controller = true
                }
            }
        }
        else if(fragmentName.equals("WalletFragment")){
            holder.itemView.setOnClickListener {
                holder.binding.starImageRecyclerRow.visibility = View.GONE
                if(controller==null){
                    holder.binding.relativeLayoutBought.visibility = View.VISIBLE
                    holder.binding.relativeLayoutSold.visibility = View.VISIBLE
                    controller = true
                }
                else if(controller as Boolean){
                    holder.binding.relativeLayoutBought.visibility = View.GONE
                    holder.binding.relativeLayoutSold.visibility = View.GONE
                    controller = false
                }
                else{
                    holder.binding.relativeLayoutBought.visibility = View.VISIBLE
                    holder.binding.relativeLayoutSold.visibility = View.VISIBLE
                    controller = true
                }
            }
        }
        else{
            println("Wrong fragment name")
        }
    }

    private fun boughtClicked(holder: ViewHolder){
        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(holder.itemView.context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    val documentId = value.documents[0].id
                    firestore.collection("Users").document(documentId).get().addOnSuccessListener {
                        val wallet = it.get("wallet") as HashMap<String, Float>
                        holder.binding.boughtButtonRecyclerRow.setOnClickListener {
                            val amount = holder.binding.boughtAmountTextRecyclerRow.text.toString()
                            val cryptoId = holder.binding.cryptoIdTextRecyclerRow.text.toString()
                            if(!amount.equals("")){
                                if(wallet.contains(cryptoId)){
                                    wallet.set(cryptoId, (amount.toFloat() + wallet.get(cryptoId)!!))
                                }
                                else{
                                    wallet.put(cryptoId, amount.toFloat())
                                }
                                firestore.collection("Users").document(documentId).update("wallet", wallet).addOnSuccessListener {
                                    holder.binding.starImageRecyclerRow.visibility = View.VISIBLE
                                    holder.binding.cryptoAmountTextRecyclerRow.visibility = View.GONE
                                    holder.binding.cryptoTotalTextRecyclerRow.visibility = View.GONE

                                    holder.binding.boughtAmountTextRecyclerRow.text = null
                                    holder.binding.soldAmountTextRecyclerRow.text = null

                                    holder.binding.relativeLayoutBought.visibility = View.GONE
                                    holder.binding.relativeLayoutSold.visibility = View.GONE

                                    (holder.itemView.context.applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(holder.itemView.windowToken, 0)
                                }.addOnFailureListener {
                                    Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                                }

                            }
                            else{
                                Toast.makeText(holder.itemView.context, "Enter Amount", Toast.LENGTH_LONG).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun soldClicked(holder: ViewHolder){
        firestore.collection("Users").whereEqualTo("email", email).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(holder.itemView.context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    val documentId = value.documents[0].id
                    firestore.collection("Users").document(documentId).get().addOnSuccessListener {
                        val wallet = it.get("wallet") as HashMap<String, Float>
                        holder.binding.soldButtonRecyclerRow.setOnClickListener {
                            val amount = holder.binding.soldAmountTextRecyclerRow.text.toString()
                            val cryptoId = holder.binding.cryptoIdTextRecyclerRow.text.toString()
                            if(!amount.equals("")){
                                if(wallet.contains(cryptoId)){
                                    val walletAmount = wallet.get(cryptoId)!!
                                    if(amount.toFloat() > walletAmount){
                                        Toast.makeText(holder.itemView.context, "You don't have enough coins to do this", Toast.LENGTH_LONG).show()
                                    }
                                    else if(amount.toFloat() == walletAmount){
                                        wallet.remove(cryptoId)
                                    }
                                    else{
                                        wallet.set(cryptoId, (walletAmount - amount.toFloat()))
                                    }
                                    firestore.collection("Users").document(documentId).update("wallet", wallet).addOnSuccessListener {
                                        holder.binding.starImageRecyclerRow.visibility = View.VISIBLE
                                        holder.binding.cryptoAmountTextRecyclerRow.visibility = View.GONE
                                        holder.binding.cryptoTotalTextRecyclerRow.visibility = View.GONE

                                        holder.binding.boughtAmountTextRecyclerRow.text = null
                                        holder.binding.soldAmountTextRecyclerRow.text = null

                                        holder.binding.relativeLayoutBought.visibility = View.GONE
                                        holder.binding.relativeLayoutSold.visibility = View.GONE

                                        (holder.itemView.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(holder.itemView.windowToken, 0)
                                    }.addOnFailureListener {
                                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                                }
                                else{
                                    Toast.makeText(holder.itemView.context, "You don't have any coins", Toast.LENGTH_LONG).show()
                                }
                            }
                            else{
                                Toast.makeText(holder.itemView.context, "Enter Amount", Toast.LENGTH_LONG).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}