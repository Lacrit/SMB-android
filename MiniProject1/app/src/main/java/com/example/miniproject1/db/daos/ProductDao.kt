package com.example.miniproject1.db.daos

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.miniproject1.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getProducts() : LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProduct(id: Int) : Product

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Update
    fun update(product: Product)

    // Provider
    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductWithCursor(id: Int): Cursor?

    @Query("SELECT * FROM product")
    fun getProductsWithCursor(): Cursor?
}