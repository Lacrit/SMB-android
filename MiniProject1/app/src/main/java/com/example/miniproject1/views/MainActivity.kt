package com.example.miniproject1.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject1.databinding.ActivityMainBinding
import com.example.miniproject1.views.products.ProductListActivity
import com.example.miniproject1.shared.SharedPrefs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPrefs(this);
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun productListHandler(view: View) {
        val intent = Intent(this, ProductListActivity::class.java);
        startActivity(intent)
    }
    fun optionsHandler(view: View) {
        val intent = Intent(this, OptionsActivity::class.java);
        startActivity(intent)
    }

}