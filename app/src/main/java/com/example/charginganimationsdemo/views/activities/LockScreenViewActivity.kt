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
import com.example.charginganimationsdemo.interfaces.OnDoubleClickListener
import com.example.charginganimationsdemo.interfaces.OnSingleClickListener

class LockScreenViewActivity : AppCompatActivity(), OnDoubleClickListener, OnSingleClickListener {

    private lateinit var binding: ActivityLockScreenViewBinding
    private var lastClickTime: Long = 0
    private val doubleClickThreshold: Long = 500

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

        getAnimation()

//        binding.root.setOnClickListener {
//            doubleClickHandler.handleClick()
//            onSingleClick()
//        }

        binding.root.setOnClickListener {
            if (isDoubleClick()) {
                onDoubleClick()
            } else {
                onSingleClick()
            }
            binding.imgClick.setVisibility(View.VISIBLE)
            Handler().postDelayed({ binding.imgClick.setVisibility(View.INVISIBLE) }, 2000)
        }

        animationSetting()

    }

    // Animation Setting
    private fun animationSetting() {

        val durationPref = getSharedPreferences("Spinner", MODE_PRIVATE)


        // Play Duration
        if (durationPref.contains("playDuration")) {
            val playDurationMillis = durationPref.getLong("playDuration", 5000)

            if (playDurationMillis.toInt() != 0) {
                Handler().postDelayed({
                    finish()
                }, playDurationMillis)
            }
        }

        // Show Battery
        if (durationPref.getBoolean("featureEnabled", true)) {
            binding.layoutBattery.visibility = View.GONE
        } else {
            binding.layoutBattery.visibility = View.VISIBLE
        }



    }

    // Lottie Animation
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

    }

    // Unregister Battery Percentage
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    private fun isDoubleClick(): Boolean {
        val clickTime = System.currentTimeMillis()
        val isDoubleClick = clickTime - lastClickTime < doubleClickThreshold
        lastClickTime = clickTime
        return isDoubleClick
    }

    // Double Click
    override fun onDoubleClick() {
        val animationSharedPref = getSharedPreferences("Spinner", MODE_PRIVATE)
        if (animationSharedPref.getBoolean("closingMethod", true)) {
            finish()
        }

    }


    // Single Click
    override fun onSingleClick() {
        val animationSharedPref = getSharedPreferences("Spinner", MODE_PRIVATE)
        if (!animationSharedPref.getBoolean("closingMethod", true)) {
            finish()
        } else {
            binding.imgClick.setVisibility(View.VISIBLE)
            Handler().postDelayed({ binding.imgClick.setVisibility(View.INVISIBLE) }, 2000)
        }

    }

}