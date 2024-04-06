package com.example.charginganimationsdemo.views.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.databinding.ActivityCustomBinding
import com.example.charginganimationsdemo.databinding.ActivityDefaultAnimationBinding
import com.example.charginganimationsdemo.services.Service

class CustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("SetAnimation", MODE_PRIVATE)
        editor = sharedPref.edit()
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

            layoutVideo.visibility = View.GONE

//            animationView.setAnimation(anim)

            imgPreview.setOnClickListener {
                ivOpenSettings.visibility = View.GONE
                btApply.visibility = View.GONE
                ivBack.visibility = View.GONE
                imgPreview.visibility = View.GONE
                layoutBattery.visibility = View.VISIBLE
            }

            root.setOnClickListener {

                ivOpenSettings.visibility = View.VISIBLE
                btApply.visibility = View.VISIBLE
                ivBack.visibility = View.VISIBLE
                imgPreview.visibility = View.VISIBLE
                layoutBattery.visibility = View.GONE
            }


            btApply.setOnClickListener {

//                editor.putInt("animation", anim)
//                editor.apply()
//                startService(Intent(this@CustomActivity, Service::class.java))

            }

            ivOpenSettings.setOnClickListener {

                startActivity(Intent(this@CustomActivity, SettingsActivity::class.java))
            }

        }
    }
}