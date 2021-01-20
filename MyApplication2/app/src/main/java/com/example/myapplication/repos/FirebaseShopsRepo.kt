package com.example.myapplication.repos

import com.example.myapplication.models.Shop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseShopsRepo {


    private val auth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance().getReference(auth.currentUser!!.uid).child("shops")

    fun insertOrUpdate(shop: Shop) {
        database.child(shop.id).setValue(shop)
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

    fun delete(shop: Shop) {
        database.child(shop.id).removeValue()
    }


}