package com.example.miniproject1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproject1.db.daos.ProductDao
import com.example.miniproject1.models.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductDb : RoomDatabase() {

    abstract fun productDao() : ProductDao

    companion object {
        private var instance : ProductDb? = null

        fun getDatabase(context : Context) : ProductDb {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDb::class.java,
                    "ProductDb"
                ).allowMainThreadQueries().build()
            }
            return instance as ProductDb
        }
    }
}