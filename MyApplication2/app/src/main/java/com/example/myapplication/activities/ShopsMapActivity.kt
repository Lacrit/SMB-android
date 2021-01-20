package com.example.myapplication.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.models.Shop
import com.example.myapplication.repos.FirebaseShopsRepo
import com.example.myapplication.repos.OnGetDataListener
import com.example.myapplication.utilities.GeofenceHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue

class ShopsMapActivity : AppCompatActivity(), OnMapReadyCallback{

    private val TAG = "MapsActivity"

    private lateinit var mMap: GoogleMap
    private lateinit var repo: FirebaseShopsRepo

    private lateinit var geofencingClient : GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper

    private var shopList: ArrayList<Shop> = ArrayList()

    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
    private val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = FirebaseShopsRepo()
        setContentView(R.layout.activity_shops_map)

        val perm = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perm, 0)
        }

        requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 0)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true

        val con = this
        repo.getAll(object : OnGetDataListener {
            override fun onSuccess(data: DataSnapshot) {
                val shops = data.getValue<HashMap<String, Shop>>()
                if (shops != null) {
                    shopList = ArrayList(shops.values)
                    var marker: Marker? = null
                    for (shop in shopList) {
                        marker = mMap.addMarker(
                            MarkerOptions().position(LatLng(shop.lat, shop.lng)).title(
                                shop.name
                            )
                        )
                        marker.tag = shop.id
                        addCircle(marker.position, shop.radius)
                        addGeofence(marker.position, shop.radius.toFloat(), shop.id);
                    }

                    LocationServices.getFusedLocationProviderClient(con).lastLocation
                        .addOnCompleteListener {
                            if (it.result != null) {
                                mMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            it.result.latitude,
                                            it.result.longitude
                                        ), 18f
                                    )
                                )
                            }
                        }


                }

            }

        })

        mMap.setOnMapClickListener {
            val intent = Intent(this, UpsertShopActivity::class.java)
            intent.putExtra("lat", it.latitude)
            intent.putExtra("lng", it.longitude)
            startActivity(intent)
        }

        mMap.setOnMarkerClickListener{
            val id = it.tag
            val intent = Intent(this, UpsertShopActivity::class.java)
            intent.putExtra("id", id.toString())
            startActivity(intent)
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.create_marker, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, ShopListActivity::class.java);
        startActivity(intent);
        finish()
        return true
    }

    fun addCircle(position: LatLng, radius: Long) {
        val circleOptions = CircleOptions()
        circleOptions.center(position)
        circleOptions.radius(radius.toDouble())
        circleOptions.strokeColor(Color.argb(255, 0, 109, 255))
        circleOptions.fillColor(Color.argb(64, 137, 209, 254))
        circleOptions.strokeWidth(4F)
        mMap.addCircle(circleOptions)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray
    ) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                mMap.isMyLocationEnabled = true
            } else {
                //We do not have the permission..
            }
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show()
            } else {
                //We do not have the permission..
                Toast.makeText(this,
                    "Background location access is neccessary for geofences to trigger...",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun addGeofence(latLng: LatLng, radius: Float, id : String) {
        val geofence: Geofence = geofenceHelper.getGeofence(id, latLng, radius,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

        val geofencingRequest: GeofencingRequest = geofenceHelper.getGeofencingRequest(geofence)

        val con = this
        val pendingIntent: PendingIntent? = geofenceHelper.getPendingIntent()
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener(object : OnSuccessListener<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    Toast.makeText(con, "added", Toast.LENGTH_SHORT).show()
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: java.lang.Exception) {
                    val errorMessage: String = geofenceHelper.getErrorString(e)
                    Toast.makeText(con, "onFailure: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            })
    }

}

