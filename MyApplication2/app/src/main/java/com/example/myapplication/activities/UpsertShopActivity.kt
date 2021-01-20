package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityUpsertShopBinding
import com.example.myapplication.models.Product
import com.example.myapplication.models.Shop
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.FirebaseShopsRepo
import com.example.myapplication.repos.OnGetDataListener
import com.example.myapplication.utilities.SharedPrefs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import java.util.*

class UpsertShopActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var radius: EditText
    private var shop: Shop? = null
    private lateinit var repo : FirebaseShopsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefs(this);
        repo = FirebaseShopsRepo()
        val binding = ActivityUpsertShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = binding.nameEdit
        description = binding.descriptionEdit
        radius = binding.radiusEdit
        var tmpId = ""
        if (intent != null && intent.extras != null)
            tmpId = intent.extras!!.getString("id", "")
        if (tmpId != "") {
            repo.getById(tmpId, object : OnGetDataListener {
                override fun onSuccess(data: DataSnapshot) {
                    shop = data.getValue<Shop>()
                    if (shop != null) {
                        name.setText(shop!!.name)
                        description.setText(shop!!.description)
                        radius.setText(shop!!.radius.toString())
                    }
                }
            })
        }
    }

    fun save(view: View?) {
        val name = name.text.toString()
        val description = description.text.toString()
        val radius = radius.text.toString().toLong()
        if (shop != null) {
            shop!!.name = name
            shop!!.description = description
            shop!!.radius = radius
            repo.insertOrUpdate(shop!!)
        } else {
            val lat = intent.extras!!.getDouble("lat", 0.0)
            val lng = intent.extras!!.getDouble("lng", 0.0)
            
            val p = Shop(UUID.randomUUID().toString(), name, description, lat, lng, radius)
            repo.insertOrUpdate(p)
        }
        val shopMap = Intent(this, ShopsMapActivity::class.java)
        startActivity(shopMap)
        finish()
    }

}