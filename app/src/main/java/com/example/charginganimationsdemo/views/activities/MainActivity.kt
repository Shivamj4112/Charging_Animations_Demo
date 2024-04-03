package com.example.charginganimationsdemo.views.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.charginganimationsdemo.databinding.ActivityMainBinding
import com.example.charginganimationsdemo.services.Service

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val anim = intent.getIntExtra("anim", 0)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        binding.apply {

            animationView.setAnimation(anim)

            imgPreview.setOnClickListener {

                imgOpenSettings.visibility = View.GONE
                btApply.visibility = View.GONE
                ivBack.visibility = View.GONE
                imgPreview.visibility = View.GONE
                layoutBattery.visibility = View.VISIBLE
            }

            root.setOnClickListener {

                imgOpenSettings.visibility = View.VISIBLE
                btApply.visibility = View.VISIBLE
                ivBack.visibility = View.VISIBLE
                imgPreview.visibility = View.VISIBLE
                layoutBattery.visibility = View.GONE
            }


            btApply.setOnClickListener {
                startService(Intent(this@MainActivity, Service::class.java))
            }

        }
    }

}