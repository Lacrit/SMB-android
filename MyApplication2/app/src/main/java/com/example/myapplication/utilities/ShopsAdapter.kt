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
import com.example.myapplication.activities.UpsertShopActivity
import com.example.myapplication.databinding.ProductBinding
import com.example.myapplication.databinding.ShopBinding
import com.example.myapplication.models.Product
import com.example.myapplication.models.Shop
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.FirebaseShopsRepo
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

class ShopsAdapter(private val repo: FirebaseShopsRepo, var list: ArrayList<Shop>, ref: DatabaseReference) :
    RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>() {



    init {
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val map=snapshot.value as HashMap<*,*>

                CoroutineScope(IO).launch {
                    val p=Shop(map["id"].toString(), map["name"].toString(), map["description"].toString(), map["lat"].toString().toDouble(), map["lng"].toString().toDouble(), map["radius"].toString().toLong() )
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
                            oldP.description=map["description"].toString()
                            oldP.lat=map["lat"].toString().toDouble()
                            oldP.lng=map["lng"].toString().toDouble()
                            oldP.radius=map["radius"].toString().toLong()

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
    class ShopViewHolder(val binding: ShopBinding) : RecyclerView.ViewHolder(binding.root) {
        var shop: Shop? = null

        init {
            SharedPrefs(binding.root.context)
        }

        fun setAll(it: Shop) {
            val name = binding.name
            val description = binding.description
            val radius = binding.radius
            val lng = binding.lng
            val lat = binding.lat

            shop = it
            name.text = it.name
            description.text = it.description
            radius.text = it.radius.toString()
            lng.text = it.lng.toString()
            lat.text = it.lat.toString()
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ShopViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ShopBinding.inflate(inflater, viewGroup, false)
        val shopViewHolder = ShopViewHolder(binding)

        binding.root.setOnLongClickListener { v: View? ->
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                repo.delete(shopViewHolder.shop!!)
                
                notifyDataSetChanged()
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            builder.setTitle(R.string.deleteProductTitle)
            val dialog = builder.create().show()
            true
        }

        binding.root.setOnClickListener { v: View ->
            val intent = Intent(v.context, UpsertShopActivity::class.java)
            intent.putExtra("id", shopViewHolder.shop!!.id)
            (v.context as Activity).startActivityForResult(intent, 0)
        }

        return shopViewHolder
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.setAll(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}


