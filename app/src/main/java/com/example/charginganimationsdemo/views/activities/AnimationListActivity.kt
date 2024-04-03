package com.example.charginganimationsdemo.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.adapter.ItemAdapter
import com.example.charginganimationsdemo.databinding.ActivityAnimationListBinding

class AnimationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationListBinding
    private lateinit var adapter: ItemAdapter
    private var anim = listOf(
        R.raw.anim10,
        R.raw.anim11,
        R.raw.anim12,
        R.raw.anim14,
        R.raw.anim15,
        R.raw.anim18,
        R.raw.anim19,
        R.raw.anim20
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            adapter = ItemAdapter {

                val intent = Intent(this@AnimationListActivity, MainActivity::class.java)
                intent.putExtra("anim", it)
                startActivity(intent)
            }


            recylerview.layoutManager = GridLayoutManager(this@AnimationListActivity, 2)
            recylerview.adapter = adapter

            adapter.updateData(anim)
        }
    }
}