package com.example.miniproject1.repositories

import com.example.miniproject1.db.daos.ProductDao
import com.example.miniproject1.models.Product

class ProductRepo(private val productDao : ProductDao) {
    val products = productDao.getProducts()

    fun getProductById(id : Int) : Product? {
        return productDao.getProduct(id)
    }

    fun insert(product : Product) {
        productDao.insert(product)
    }

    fun delete(product: Product) {
        productDao.delete(product)
    }

    fun update(product: Product) {
        productDao.update(product)
    }
}