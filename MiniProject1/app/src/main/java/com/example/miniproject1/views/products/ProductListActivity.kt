package com.example.miniproject1.views.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniproject1.databinding.ProductListActivityBinding
import com.example.miniproject1.viewModels.ProductViewModel
import com.example.miniproject1.models.Product
import com.example.miniproject1.shared.SharedPrefs

class ProductListActivity : AppCompatActivity() {
    private lateinit var productsAdapter: ProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefs(this);
        val binding = ProductListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val productViewModel = ProductViewModel(application)

        productsAdapter = ProductsAdapter(productViewModel)
        productViewModel.products.observe(this, Observer {products ->
            products?.let {
                productsAdapter.setProducts(it as MutableList<Product>)
            }
        })

        binding.recyclerView.adapter = productsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun changeToUpsert(view: View?) {
        val intent = Intent(this, UpsertProductActivity::class.java)
        startActivity(intent)
    }
}