package com.example.charginganimationsdemo.views.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {

            ivBack.setOnClickListener {
                finish()
            }


            val durationItems = arrayOf("5 secs", "10 secs", "30 secs", "1 min", "loop")
            val durationAdapter =
                ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, durationItems)
            durationAdapter.setDropDownViewResource(R.layout.spinner_item)
            spinnerDuration.adapter = durationAdapter

            val closingMethodItems = arrayOf("Double Click", "Single Click")
            val closingMethodAdapter =
                ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, closingMethodItems)
            closingMethodAdapter.setDropDownViewResource(R.layout.spinner_item)
            spinnerClosingMethod.adapter = closingMethodAdapter

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}