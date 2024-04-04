package com.example.charginganimationsdemo.views.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("Spinner", MODE_PRIVATE)
        editor = sharedPref.edit()


        binding.apply {

            ivBack.setOnClickListener {
                finish()
            }


            playDuration()
            closingMethod()


            switchPer.isChecked = sharedPref.getBoolean("showPercentage", true)

            switchPer.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    editor.putBoolean("showPercentage", true)
                    editor.apply()
                } else {
                    editor.putBoolean("showPercentage", false)
                    editor.apply()
                }

            }


        }


    }

    private fun ActivitySettingsBinding.playDuration() {
        val durationItems = arrayOf("5 secs", "10 secs", "30 secs", "1 min", "loop")
        val durationAdapter =
            ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, durationItems)
        durationAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerDuration.adapter = durationAdapter

        spinnerDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (position) {

                    0 -> {
                        editor.putLong("playDuration", 5000)
                        editor.apply()
                    }

                    1 -> {
                        editor.putLong("playDuration", 10000)
                        editor.apply()
                    }

                    2 -> {
                        editor.putLong("playDuration", 30000)
                        editor.apply()
                    }

                    3 -> {
                        editor.putLong("playDuration", 100000)
                        editor.apply()
                    }

                    4 -> {
                        editor.putLong("playDuration", 0)
                        editor.apply()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun ActivitySettingsBinding.closingMethod() {
        val closingMethodItems = arrayOf("Double Click", "Single Click")
        val closingMethodAdapter =
            ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, closingMethodItems)
        closingMethodAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerClosingMethod.adapter = closingMethodAdapter

        spinnerClosingMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        editor.putBoolean("closingMethod", true)
                        editor.apply()
                    }

                    1 -> {
                        editor.putBoolean("closingMethod", false)
                        editor.apply()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}