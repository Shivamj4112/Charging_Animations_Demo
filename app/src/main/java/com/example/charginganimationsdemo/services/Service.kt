package com.example.charginganimationsdemo.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.views.activities.LockScreenViewActivity


class Service : Service() {

    companion object {
        private const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val NOTIFICATION_ID = 1
    }

    private var connectionChangedReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when {
                    intent.action == Intent.ACTION_POWER_CONNECTED ->
                        powerWasConnected(context)

                    intent.action == Intent.ACTION_POWER_DISCONNECTED ->
                        powerWasDisconnected(context)
                }
            }
        }

    override fun onCreate() {
        val connectionChangedIntent = IntentFilter()
        connectionChangedIntent.addAction(Intent.ACTION_POWER_CONNECTED)
        connectionChangedIntent.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(connectionChangedReceiver, connectionChangedIntent)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)

            startForeground(NOTIFICATION_ID, createNotification())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForeground(
                NOTIFICATION_ID,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            )


        }
    }

    override fun onStartCommand(
        resultIntent: Intent, resultCode: Int, startId: Int
    ): Int {
        return startId
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectionChangedReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun powerWasConnected(context: Context) {
        Log.d("TAG", "powerWasConnected: Power is connected")
        val lockScreenIntent = Intent(context, LockScreenViewActivity::class.java)
        lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(lockScreenIntent)

    }

    private fun powerWasDisconnected(context: Context) {

        Log.d("TAG", "powerWasDisconnected: Power is disconnected")
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, LockScreenViewActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }
}