    package com.example.charginganimationsdemo.views.activities

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import com.example.charginganimationsdemo.databinding.ActivityMainBinding
    import com.example.charginganimationsdemo.services.Service

    class MainActivity : AppCompatActivity() {

        private lateinit var binding : ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)


                binding.btApply.setOnClickListener {

                    startService(Intent(this, Service::class.java))
                }


        }
    }