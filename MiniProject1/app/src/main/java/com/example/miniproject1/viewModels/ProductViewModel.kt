package com.example.miniproject1.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.miniproject1.db.ProductDb
import com.example.miniproject1.models.Product
import com.example.miniproject1.repositories.ProductRepo

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repo : ProductRepo
    val products : LiveData<List<Product>>

    init {
        val productDao = ProductDb.getDatabase(application).productDao()
        repo = ProductRepo(productDao)
        products = repo.products
    }

    fun getProductById(id : Int) : Product? = repo.getProductById(id)

    fun insert(product : Product) = repo.insert(product)

    fun delete(product: Product) = repo.delete(product)

    fun update(product: Product) = repo.update(product)
}