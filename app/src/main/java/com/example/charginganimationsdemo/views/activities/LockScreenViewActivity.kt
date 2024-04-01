package com.example.charginganimationsdemo.views.activities

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
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
import com.example.charginganimationsdemo.interfaces.SingleClickListener
import com.example.charginganimationsdemo.services.Service

class LockScreenViewActivity : AppCompatActivity(), OnDoubleClickListener , OnSingleClickListener{

    private lateinit var binding: ActivityLockScreenViewBinding
    private val doubleClickHandler = DoubleClickHandler(this)

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                context?.registerReceiver(null, ifilter)
            }

            val level: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct: Float = (level ?: 0).toFloat() / (scale ?: 100).toFloat() * 100
//            val roundedBatteryPct = batteryPct.toInt() // Round the battery percentage

            binding.tvPercentage.text = "$batteryPct %"

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        binding = ActivityLockScreenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        binding.animationView.setAnimation(R.raw.anim20)

        binding.root.setOnClickListener {
            doubleClickHandler.handleClick()
        }
        binding.root.setOnClickListener {
            doubleClickHandler.handleClick()
            onSingleClick()
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
//        stopService(Intent(this, Service::class.java))
    }

    override fun onDoubleClick() {
        finish()
    }

    override fun onSingleClick() {
        binding.imgClick.setVisibility(View.VISIBLE)
        Handler().postDelayed({ binding.imgClick.setVisibility(View.INVISIBLE) }, 2000)
    }


}