package com.example.myapplication.broadcastreceiver

import NotificationHelper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.myapplication.activities.ShopsMapActivity
import com.example.myapplication.models.Shop
import com.example.myapplication.repos.FirebaseShopsRepo
import com.example.myapplication.repos.OnGetDataListener
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceBroadcastReceiv"
    private lateinit var shop : Shop

    override fun onReceive(context: Context, intent: Intent) {

        val notificationHelper = NotificationHelper(context)
        val repo = FirebaseShopsRepo()
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        val geofenceList = geofencingEvent.triggeringGeofences
        for (geofence in geofenceList) {
            if (geofence.requestId != null) {
                repo.getById(geofence.requestId, object : OnGetDataListener {
                    override fun onSuccess(data: DataSnapshot) {
                        val transitionType = geofencingEvent.geofenceTransition
                        val shop = data.getValue<Shop>()
                        when (transitionType) {
                            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                                notificationHelper.sendHighPriorityNotification(
                                    "GEOFENCE_TRANSITION_ENTER", "Welcome to the ${shop!!.name} shop", ShopsMapActivity::class.java
                                )
                            }
                            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                                notificationHelper.sendHighPriorityNotification(
                                    "GEOFENCE_TRANSITION_EXIT", "Goodbye! Hope to see you again in ${shop!!.name}", ShopsMapActivity::class.java
                                )
                            }
                        }

                    }
                })
            }
        }

    }
}