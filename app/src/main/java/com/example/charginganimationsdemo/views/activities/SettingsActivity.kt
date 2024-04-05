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
        val durationItems = arrayOf("3 secs", "5 secs", "10 secs", "30 secs", "1 min", "loop")
        val durationAdapter =
            ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, durationItems)
        durationAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerDuration.adapter = durationAdapter

        spinnerDuration.setSelection(durationItems.indexOf(getSavedDuration()))

        spinnerDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDuration = durationItems[position]
                saveDuration(selectedDuration)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun saveDuration(selectedDuration: String) {
        editor.putString("selectedDuration", selectedDuration)
        editor.apply()
    }

    private fun getSavedDuration(): String {
        return sharedPref.getString("selectedDuration", "3 secs") ?: "3 secs"
    }

    private fun ActivitySettingsBinding.closingMethod() {
        val closingMethodItems = arrayOf("Double Click", "Single Click" , "Swipe")
        val closingMethodAdapter =
            ArrayAdapter(this@SettingsActivity, R.layout.spinner_item, closingMethodItems)
        closingMethodAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerClosingMethod.adapter = closingMethodAdapter

        spinnerClosingMethod.setSelection(closingMethodItems.indexOf(getSavedClosingMethod()))

        spinnerClosingMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedClosingMethod = closingMethodItems[position]
                saveClosingMethod(selectedClosingMethod)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun saveClosingMethod(selectedClosingMethod: String) {
        editor.putString("selectedClosingMethod", selectedClosingMethod)
        editor.apply()
    }

    private fun getSavedClosingMethod(): String {
        return sharedPref.getString("selectedClosingMethod", "Double Click") ?: "Double Click"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}