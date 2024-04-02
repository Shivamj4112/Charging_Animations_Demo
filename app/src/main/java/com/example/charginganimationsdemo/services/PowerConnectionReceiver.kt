package com.example.charginganimationsdemo.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_POWER_CONNECTED == intent.action) {

            val serviceIntent = Intent(context, Service::class.java)
            context.startService(serviceIntent)
//            ContextCompat.startForegroundService(context,serviceIntent)
        }
    }
}

    