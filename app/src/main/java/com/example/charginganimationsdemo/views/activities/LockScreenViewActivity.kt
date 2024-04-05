package com.example.charginganimationsdemo.views.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.databinding.ActivityLockScreenViewBinding
import com.example.charginganimationsdemo.interfaces.OnDoubleClickListener
import com.example.charginganimationsdemo.interfaces.OnSingleClickListener
import com.ncorti.slidetoact.SlideToActView


class LockScreenViewActivity : AppCompatActivity(), OnDoubleClickListener, OnSingleClickListener {

    private lateinit var binding: ActivityLockScreenViewBinding
    private var lastClickTime: Long = 0
    private val doubleClickThreshold: Long = 500

    private var connectionChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when {
                    intent.action == Intent.ACTION_POWER_CONNECTED ->{}

                    intent.action == Intent.ACTION_POWER_DISCONNECTED ->{}
                }
            }
        }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context?.registerReceiver(null, ifilter)
                }
            val level: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct: Float = (level ?: 0).toFloat() / (scale ?: 100).toFloat() * 100
            val roundedBatteryPct = batteryPct.toInt()

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


        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        binding = ActivityLockScreenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val connectionChangedIntent = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(connectionChangedReceiver, connectionChangedIntent)

        showBattery()

        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registerReceiver(powerDisconnectReceiver, IntentFilter(Intent.ACTION_POWER_DISCONNECTED))

        getAnimation()
        playDuration()


        val durationPref = getSharedPreferences("Spinner", MODE_PRIVATE)
        val selectedClosingMethodText = durationPref.getString("selectedClosingMethod", "Double Click")
        if(selectedClosingMethodText == "Swipe") {

            binding.savSlideButton.visibility = View.VISIBLE
            binding.imgClick.visibility = View.GONE

            binding.savSlideButton.onSlideCompleteListener =
                object : SlideToActView.OnSlideCompleteListener {
                    override fun onSlideComplete(view: SlideToActView) {

                        finish()
                    }
                }
        }
        else if (selectedClosingMethodText == "Single Click"){
            binding.imgClick.visibility = View.GONE
            binding.savSlideButton.visibility = View.GONE
        }
        else{
            binding.savSlideButton.visibility = View.GONE
        }



        binding.root.setOnClickListener {

            val durationPref = getSharedPreferences("Spinner", MODE_PRIVATE)
            val selectedClosingMethodText = durationPref.getString("selectedClosingMethod", "Double Click")

            if (selectedClosingMethodText == "Single Click") {
                onSingleClick()
                binding.imgClick.visibility = View.GONE
            }

            else if(selectedClosingMethodText == "Double Click"){
                binding.imgClick.visibility = View.VISIBLE
                if (isDoubleClick()) {
                    onDoubleClick()
                }
            }
            else{
                binding.imgClick.visibility = View.GONE
            }
            Handler().postDelayed({ binding.imgClick.setVisibility(View.INVISIBLE) }, 2000)

        }
    }

    private fun showBattery() {
        val sharedPref = getSharedPreferences("Spinner", MODE_PRIVATE)
        val showBattery = sharedPref.getBoolean("showPercentage", true)
        binding.layoutBattery.visibility = if (showBattery) View.VISIBLE else View.INVISIBLE
    }

    // Animation Setting
    private fun playDuration() {

        val durationPref = getSharedPreferences("Spinner", MODE_PRIVATE)

        val selectedDurationText = durationPref.getString("selectedDuration", "3 secs")
        val selectedDurationMillis = when (selectedDurationText) {
            "3 secs" -> 3000L
            "5 secs" -> 5000L
            "10 secs" -> 10000L
            "30 secs" -> 30000L
            "1 min" -> 60000L
            "loop" -> 0L
            else -> 3000L
        }

        if (selectedDurationMillis != 0L) {
            Handler().postDelayed({
                finish()
            }, selectedDurationMillis)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        unregisterReceiver(connectionChangedReceiver)
    }

    private fun isDoubleClick(): Boolean {
        val clickTime = System.currentTimeMillis()
        val isDoubleClick = clickTime - lastClickTime < doubleClickThreshold
        lastClickTime = clickTime
        return isDoubleClick
    }


    override fun onDoubleClick() { finish() }

    override fun onSingleClick() { finish() }


}