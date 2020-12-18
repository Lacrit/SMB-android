package com.example.myapplication.models

import android.database.Cursor
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Product(
    var id: String = "",
    var name: String = "",
    var price: Float = 0.0f,
    var bought: Boolean = false,
    var quantity: Float = 0.0f) {
}