package com.example.myapplication.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Shop(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var radius: Long = 5,
) {

}