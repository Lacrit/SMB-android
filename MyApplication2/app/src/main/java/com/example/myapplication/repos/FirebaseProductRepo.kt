package com.example.myapplication.repos

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.Product
import com.example.myapplication.utilities.SharedPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


interface OnGetDataListener {
    fun onSuccess(data: DataSnapshot)
}

class FirebaseProductRepo(private val sharedPrefs : SharedPrefs) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var database : DatabaseReference
     init {
         database = if (sharedPrefs.getListModeState() == SharedPrefs.INDIVIDUAL) {
             FirebaseDatabase.getInstance().getReference(auth.currentUser!!.uid).child("products")
         } else {
             FirebaseDatabase.getInstance().getReference("shared")
         }
     }

    fun insertOrUpdate(item: Product) {
        database.child(item.id).setValue(item)
    }

    fun getById(id: String, listener: OnGetDataListener) {
        database.child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listener.onSuccess(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) = Unit
            })
    }

    fun getAll(listener: OnGetDataListener) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener.onSuccess(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) = Unit
        })
    }

    fun delete(product: Product) {
        database.child(product.id).removeValue()
    }



}
