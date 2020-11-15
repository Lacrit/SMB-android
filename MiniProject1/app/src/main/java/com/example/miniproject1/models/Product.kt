package com.example.miniproject1.models

import android.database.Cursor
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name: String,
    var price: Float,
    var bought: Boolean,
    var quantity: Float) {

    companion object {
        const val KEY_ROW_ID = "key_row_id"
        const val NAME = "name"
        const val PRICE = "price"
        const val BOUGHT = "bought"
        const val QUANTITY = "quantity"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(  id: Int,
                  name: String,
                  price: Float,
                  bought: Boolean,
                  quantity: Float) : this(name, price, bought, quantity) {
        this.id = id
        this.name = name
        this.price = price
        this.bought = bought
        this.quantity = quantity

    }

    fun getProductFromCursor(cursor: Cursor): Product {
        val id = cursor.getInt(cursor.getColumnIndex(KEY_ROW_ID))
        val name =
            cursor.getString(cursor.getColumnIndex(NAME))
        val price =
            cursor.getFloat(cursor.getColumnIndex(PRICE))
        val bought =
            cursor.getInt(cursor.getColumnIndex(BOUGHT)) == 1
        val quantity =
            cursor.getFloat(cursor.getColumnIndex(QUANTITY))
        return Product(id, name, price, bought, quantity)
    }
}