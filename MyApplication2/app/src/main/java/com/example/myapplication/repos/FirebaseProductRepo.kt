package com.example.myapplication.repos

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.Product
import com.example.myapplication.utilities.SharedPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


interface OnGetDataListener {
    fun onSuccess(data: DataSnapshot)
}

class FirebaseProductRepo(private val sharedPrefs : SharedPrefs) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference(if (sharedPrefs.getListModeState() == SharedPrefs.INDIVIDUAL) auth.currentUser!!.uid else "shared")

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
