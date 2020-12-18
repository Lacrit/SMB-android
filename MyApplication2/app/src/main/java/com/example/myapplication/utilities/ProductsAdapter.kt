package com.example.myapplication.utilities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activities.UpsertProductActivity
import com.example.myapplication.databinding.ProductBinding
import com.example.myapplication.models.Product
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.OnGetDataListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ProxySelector

class ProductsAdapter(private val repo: FirebaseProductRepo, var list: ArrayList<Product>, ref: DatabaseReference) :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    init {
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val map=snapshot.value as HashMap<*,*>

                CoroutineScope(IO).launch {
                    val p=Product(map["id"].toString(), map["name"].toString(), map["price"].toString().toFloat(), map["bought"].toString().toBoolean(), map["quantity"].toString().toFloat() )
                    if(!list.contains(p)) {
                        list.add(p)
                    }
                    withContext(Main) {
                        notifyDataSetChanged()
                    }

                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val map=snapshot.value as HashMap<*,*>

                CoroutineScope(IO).launch {

                    withContext(Main) {
                        val oldP=list.find { it.id==map["id"].toString() }
                        if (oldP != null) {
                            oldP.name=map["name"].toString()
                            oldP.price=map["price"].toString().toFloat()
                            oldP.bought=map["bought"].toString().toBoolean()
                            oldP.quantity=map["quantity"].toString().toFloat()

                        }

                        notifyDataSetChanged()
                    }

                }


            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val map=snapshot.value as HashMap<*,*>

                CoroutineScope(IO).launch {
                    val oldP=list.find { it.id==map["id"].toString() }
                    list.remove(oldP)
                    withContext(Main) {
                        notifyDataSetChanged()
                    }

                }


            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    class ProductViewHolder(val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root) {
        var product: Product? = null

        init {
            SharedPrefs(binding.root.context)
        }

        fun setAll(it: Product) {
            val bought = binding.bought
            val quantity = binding.quantity
            val price = binding.price
            val name = binding.name

            product = it
            name.text = it.name
            price.text = it.price.toString()
            quantity.text = it.quantity.toString()
            bought.isChecked = it.bought
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ProductBinding.inflate(inflater, viewGroup, false)
        val productViewHolder = ProductViewHolder(binding)

        binding.root.setOnLongClickListener { v: View? ->
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
              repo.delete(productViewHolder.product!!)
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            builder.setTitle(R.string.deleteProductTitle)
            val dialog = builder.create().show()
            true
        }

        binding.root.setOnClickListener { v: View ->
            val intent = Intent(v.context, UpsertProductActivity::class.java)
            intent.putExtra("id", productViewHolder.product!!.id)
            (v.context as Activity).startActivityForResult(intent, 0)
        }

        return productViewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setAll(list[position])

        val bought = holder.binding.bought

        bought.setOnCheckedChangeListener { _, isChecked: Boolean ->
            list[position].bought = isChecked
            repo.insertOrUpdate(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}


