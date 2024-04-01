package com.example.charginganimationsdemo.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_POWER_CONNECTED == intent.action) {

            val serviceIntent = Intent(context, Service::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}

    