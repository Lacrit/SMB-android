package com.example.myapplication.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityShopListBinding
import com.example.myapplication.databinding.ProductListActivityBinding
import com.example.myapplication.databinding.UpsertProductActivityBinding
import com.example.myapplication.models.Product
import com.example.myapplication.models.Shop
import com.example.myapplication.repos.FirebaseProductRepo
import com.example.myapplication.repos.FirebaseShopsRepo
import com.example.myapplication.repos.OnGetDataListener
import com.example.myapplication.utilities.ProductsAdapter
import com.example.myapplication.utilities.SharedPrefs
import com.example.myapplication.utilities.ShopsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShopListActivity : AppCompatActivity() {

    private lateinit var shopsAdapter: ShopsAdapter

    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var radius: EditText

    private lateinit var repo : FirebaseShopsRepo

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        SharedPrefs(this);
        val binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = binding.nameEdit
        description = binding.descriptionEdit
        radius = binding.radiusEdit
        repo = FirebaseShopsRepo()

        val con=this
        var list: ArrayList<Shop> = ArrayList()
        repo.getAll(object : OnGetDataListener {
            override fun onSuccess(data: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val shops = data.getValue<HashMap<String, Shop>>()
                    if (shops != null) {
                        list = ArrayList(shops.values)
                    }
                    withContext(Dispatchers.Main) {
                        shopsAdapter = ShopsAdapter(repo, list, repo.database)
                        binding.recyclerViewShop.adapter = shopsAdapter
                        binding.recyclerViewShop.layoutManager = LinearLayoutManager(con)
                    }
                }
            }

        })
    }

    @SuppressLint("MissingPermission")
    fun save(view: View?) {
        val name = name.text.toString()
        val description = description.text.toString()
        val radius = radius.text.toString().toLong()

        LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnCompleteListener {
                if(it.result != null) {
                    val lat = it.result.latitude
                    val lng = it.result.longitude

                    val p = Shop(UUID.randomUUID().toString(), name, description, lat, lng, radius)
                    repo.insertOrUpdate(p)
                }
            }

        val shopMap = Intent(this, ShopsMapActivity::class.java)
        startActivity(shopMap)
        finish()
    }
}