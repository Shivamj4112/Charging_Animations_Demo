package com.example.charginganimationsdemo.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.example.charginganimationsdemo.interfaces.OnSingleClickListener
import com.example.charginganimationsdemo.views.CustomLockScreenView

class Service : Service(), OnSingleClickListener {

    private var customLockScreenView: CustomLockScreenView? = null
    private var windowManager: WindowManager? = null

    private val powerConnectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_POWER_CONNECTED -> powerWasConnected()
                Intent.ACTION_POWER_DISCONNECTED -> powerWasDisconnected()
            }
        }
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onCreate() {
        super.onCreate()


        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(powerConnectionReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerConnectionReceiver)
        powerWasDisconnected()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun powerWasConnected() {



        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager?

        if (customLockScreenView == null) {
            customLockScreenView = CustomLockScreenView(this)

//            customLockScreenView!!.setClick(object : OnSingleClickListener {
//                override fun onSingleClick() {
//                    Log.d("Clicks Service", "Custom view clicked!")
//
//                }
//            })
//            customLockScreenView!!.findViewById<LottieAnimationView>(R.id.animationView).setAnimation(R.raw.anim20)


            Log.d("TAG", "powerWasConnected: Power is connected")
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT,

                )
            layoutParams.gravity = Gravity.CENTER
            windowManager?.addView(customLockScreenView, layoutParams)


//            Handler().postDelayed({
//
//                customLockScreenView?.let {
//                    windowManager?.removeView(it)
//                    customLockScreenView = null
//                }
//
//            }, 5000)

        }
    }

    private fun powerWasDisconnected() {
        Log.d("TAG", "powerWasDisconnected: Power is disconnected")
        clearFilter()
    }

    private fun clearFilter() {
        customLockScreenView?.let {
            windowManager?.removeView(it)
            customLockScreenView = null
        }
    }

    override fun onSingleClick() {
        Log.d("Clicks Service222", "Custom view clicked!")
    }

}


//class Service : Service() {
//
//    companion object {
//        private const val CHANNEL_ID = "ForegroundServiceChannel"
//        private const val NOTIFICATION_ID = 1
//    }
//
//    private var connectionChangedReceiver: BroadcastReceiver =
//        object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                when {
//                    intent.action == Intent.ACTION_POWER_CONNECTED ->
//                        powerWasConnected(context)
//
//                    intent.action == Intent.ACTION_POWER_DISCONNECTED ->
//                        powerWasDisconnected(context)
//                }
//            }
//        }
//
//    override fun onCreate() {
//        val connectionChangedIntent = IntentFilter()
//        connectionChangedIntent.addAction(Intent.ACTION_POWER_CONNECTED)
//        connectionChangedIntent.addAction(Intent.ACTION_POWER_DISCONNECTED)
//        registerReceiver(connectionChangedReceiver, connectionChangedIntent)
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val serviceChannel = NotificationChannel(
//                CHANNEL_ID,
//                "Foreground Service Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(serviceChannel)
//
//            startForeground(NOTIFICATION_ID, createNotification())
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            startForeground(
//                NOTIFICATION_ID,
//                createNotification(),
//                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
//            )
//
//
//        }
//    }
//
//    override fun onStartCommand(
//        resultIntent: Intent, resultCode: Int, startId: Int
//    ): Int {
//        return startId
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(connectionChangedReceiver)
//    }
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    private fun powerWasConnected(context: Context) {
//        Log.d("TAG", "powerWasConnected: Power is connected")
//        val lockScreenIntent = Intent(context, LockScreenViewActivity::class.java)
//        lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//
//        context.startActivity(lockScreenIntent)
//
//    }
//
//    private fun powerWasDisconnected(context: Context) {
//
//        Log.d("TAG", "powerWasDisconnected: Power is disconnected")
//    }
//
//    private fun createNotification(): Notification {
//        val notificationIntent = Intent(this, LockScreenViewActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, notificationIntent,
//            PendingIntent.FLAG_MUTABLE
//        )
//
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("")
//            .setContentText("")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentIntent(pendingIntent)
//            .build()
//    }
//}