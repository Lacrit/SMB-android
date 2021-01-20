package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ProductListActivityBinding
import com.example.myapplication.models.Product
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.OnGetDataListener
import com.example.myapplication.utilities.ProductsAdapter
import com.example.myapplication.utilities.SharedPrefs
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductListActivity : AppCompatActivity() {
    private lateinit var productsAdapter: ProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = SharedPrefs(this);
        val binding = ProductListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = FirebaseProductRepo(sharedPrefs)


        val con=this
        var list: ArrayList<Product> = ArrayList()
        repo.getAll(object : OnGetDataListener {
            override fun onSuccess(data: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    var cos = data
                    val products = data.getValue<HashMap<String, Product>>()
                    if (products != null) {
                        list = ArrayList(products.values)
                    }
                    withContext(Dispatchers.Main) {
                        productsAdapter = ProductsAdapter(repo, list, repo.database)
                        binding.recyclerView.adapter = productsAdapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(con)
                    }
                }
            }

        })
    }

    fun changeToUpsert(view: View?) {
        val intent = Intent(this, UpsertProductActivity::class.java)
        startActivity(intent)
    }
}