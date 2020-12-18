package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.utilities.SharedPrefs

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent);
        finish()
        return true
    }
}