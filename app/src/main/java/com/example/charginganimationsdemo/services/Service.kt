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
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.util.CustomLockScreenView
import com.example.charginganimationsdemo.views.activities.LockScreenViewActivity
import com.example.charginganimationsdemo.views.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class Service : Service() {
//
//    private var customLockScreenView: CustomLockScreenView? = null
//    private var windowManager: WindowManager? = null
//
//    private val powerConnectionReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.action) {
//                Intent.ACTION_POWER_CONNECTED -> powerWasConnected()
//                Intent.ACTION_POWER_DISCONNECTED -> powerWasDisconnected()
//            }
//        }
//    }
//
//    @SuppressLint("MissingInflatedId", "InflateParams")
//    override fun onCreate() {
//        super.onCreate()
//
//
//        val filter = IntentFilter().apply {
//            addAction(Intent.ACTION_POWER_CONNECTED)
//            addAction(Intent.ACTION_POWER_DISCONNECTED)
//        }
//        registerReceiver(powerConnectionReceiver, filter)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(powerConnectionReceiver)
//        powerWasDisconnected()
//    }
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    private fun powerWasConnected() {
//
//        fun getBatteryPercentage(context: Context): Int {
//            val registerReceiver = context.registerReceiver(
//                null,
//                IntentFilter("android.intent.action.BATTERY_CHANGED")
//            )
//            return (registerReceiver!!.getIntExtra("level", -1)
//                .toFloat() / registerReceiver.getIntExtra("scale", -1).toFloat() * 100.0f).toInt()
//        }
//
//
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager?
//
//
//        if (customLockScreenView == null) {
//            customLockScreenView = CustomLockScreenView(this)
//
////            customLockScreenView!!.setClick(object : OnSingleClickListener {
////                override fun onSingleClick() {
////                    Log.d("Clicks Service", "Custom view clicked!")
////
////                }
////            })
//            customLockScreenView!!.findViewById<LottieAnimationView>(R.id.animationView)
//                .setAnimation(R.raw.anim20)
//
//
//            Log.d("TAG", "powerWasConnected: Power is connected")
//            val layoutParams = WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//                else
//                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
//                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
//                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
//                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
//
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
//                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
//                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                PixelFormat.TRANSLUCENT
//
//
//            )
//            layoutParams.gravity = Gravity.CENTER
//            windowManager?.addView(customLockScreenView, layoutParams)
//
//            customLockScreenView?.systemUiVisibility =
//                customLockScreenView?.systemUiVisibility ?: (0 or
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                        View.SYSTEM_UI_FLAG_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//
//
////            Handler().postDelayed({
////
////                customLockScreenView?.let {
////                    windowManager?.removeView(it)
////                    customLockScreenView = null
////                }
////
////            }, 5000)
//
//        }
//    }
//
//    private fun powerWasDisconnected() {
//        Log.d("TAG", "powerWasDisconnected: Power is disconnected")
//        clearFilter()
//    }
//
//    private fun clearFilter() {
//        customLockScreenView?.let {
//            windowManager?.removeViewImmediate(it)
//            customLockScreenView = null
//        }
//    }
//private fun clearFilter() {

//        customLockScreenView?.let {
//            windowManager?.removeViewImmediate(it)
//            customLockScreenView = null
//        }
//}
//}



class Service : Service() {

    companion object {
        private const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val NOTIFICATION_ID = 1
    }
    private lateinit var job: Job

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
        job = Job()
        val connectionChangedIntent = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
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
//
//        CoroutineScope(Dispatchers.IO + job).launch {
            val lockScreenIntent = Intent(context, LockScreenViewActivity::class.java)
            lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)


//            withContext(Dispatchers.Main) {
//                // Update UI if needed after background task completion
                context.startActivity(lockScreenIntent)
//            }
//        }

    }


    private fun powerWasDisconnected(context: Context) {

        Log.d("TAG", "powerWasDisconnected: Power is disconnected")
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
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