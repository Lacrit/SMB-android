package com.example.myapplication.activities

import android.app.NotificationChannel
import android.app.NotificationManager
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

    private val CHANNEL_NAME = "High priority channel"
    private val CHANNEL_ID = "com.example.notifications$CHANNEL_NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPrefs(this);
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createChannels()
    }

    fun productListHandler(view: View) {
        val intent = Intent(this, ProductListActivity::class.java);
        startActivity(intent)
    }
    fun optionsHandler(view: View) {
        val intent = Intent(this, OptionsActivity::class.java);
        startActivity(intent)
    }
    fun shopsMapHandler(view: View) {
        val intent = Intent(this, ShopsMapActivity::class.java);
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

    private fun createChannels() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(notificationChannel)

    }
}