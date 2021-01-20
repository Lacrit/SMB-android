package com.example.myapplication.utilities

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.example.myapplication.broadcastreceiver.GeofenceBroadcastReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng


class GeofenceHelper(base: Context?) : ContextWrapper(base) {

    private var pendingIntent: PendingIntent? = null

    fun getGeofencingRequest(geofence: Geofence) : GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(ID: String, pos: LatLng, radius: Float, transitionTypes: Int) : Geofence {
        return Geofence.Builder()
            .setCircularRegion(pos.latitude, pos.longitude, radius)
            .setRequestId(ID)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(2000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }


    fun getPendingIntent() : PendingIntent? {
        if (pendingIntent != null) {
            return pendingIntent
        }
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,2607,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    fun getErrorString(e: Exception): String {
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->  "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->  "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->  "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }
}