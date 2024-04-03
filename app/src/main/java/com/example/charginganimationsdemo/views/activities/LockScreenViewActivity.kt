package com.example.charginganimationsdemo.views.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.databinding.ActivityLockScreenViewBinding
import com.example.charginganimationsdemo.interfaces.DoubleClickHandler
import com.example.charginganimationsdemo.interfaces.OnDoubleClickListener
import com.example.charginganimationsdemo.interfaces.OnSingleClickListener

class LockScreenViewActivity : AppCompatActivity(), OnDoubleClickListener, OnSingleClickListener {

    private lateinit var binding: ActivityLockScreenViewBinding
    private val doubleClickHandler = DoubleClickHandler(this)
//    private lateinit var sharedPref : SharedPreferences
//    private lateinit var editor: SharedPreferences.Editor

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context?.registerReceiver(null, ifilter)
                }
            val level: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct: Float = (level ?: 0).toFloat() / (scale ?: 100).toFloat() * 100
            val roundedBatteryPct = batteryPct.toInt() // Round the battery percentage

            binding.tvPercentage.text = "$roundedBatteryPct %"
        }
    }

    private val powerDisconnectReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_POWER_DISCONNECTED) {
                finish()
            }

        }
    }
//    private val powerConnectReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            if (intent?.action == Intent.ACTION_POWER_CONNECTED) {
//                Handler().postDelayed({
//                    finish()
//                }, 3000)
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        binding = ActivityLockScreenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(powerDisconnectReceiver, IntentFilter(Intent.ACTION_POWER_DISCONNECTED))
//        registerReceiver(powerConnectReceiver, IntentFilter(Intent.ACTION_POWER_CONNECTED))

        getAnimation()

        binding.root.setOnClickListener {
            doubleClickHandler.handleClick()
            onSingleClick()
        }

        Handler().postDelayed({
            finish()
        }, 3000)

    }

    private fun getAnimation() {
        val animationSharedPref = getSharedPreferences("SetAnimation", MODE_PRIVATE)
        var animationEditor: SharedPreferences.Editor = animationSharedPref.edit()
        if (animationSharedPref.contains("animation")) {
            binding.animationView.setAnimation(
                animationSharedPref.getInt(
                    "animation",
                    R.raw.anim20
                )
            )
        } else {
            animationEditor.putInt("animation", R.raw.anim20)
            animationEditor.apply()
            binding.animationView.setAnimation(animationSharedPref.getInt("animation", 0))
        }
        //        binding.animationView.setAnimation(R.raw.anim20)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
//        stopService(Intent(this, Service::class.java))
//        finish()
    }

    override fun onDoubleClick() {
        finish()
    }

    override fun onSingleClick() {
        binding.imgClick.setVisibility(View.VISIBLE)
        Handler().postDelayed({ binding.imgClick.setVisibility(View.INVISIBLE) }, 2000)
    }

}