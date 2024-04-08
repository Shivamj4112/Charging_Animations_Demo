package com.example.charginganimationsdemo.views.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.charginganimationsdemo.adapter.ImageAdapter
import com.example.charginganimationsdemo.databinding.ActivityCustomAnimationListBinding


class CustomAnimationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomAnimationListBinding
    private lateinit var list : ArrayList<Uri>
    private lateinit var adapter : ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomAnimationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        binding.apply {


            ivAdd.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            }


            adapter = ImageAdapter()

            rvBackground.layoutManager = LinearLayoutManager(this@CustomAnimationListActivity,LinearLayoutManager.HORIZONTAL,true)
            rvBackground.adapter = adapter


            adapter.updateImage(list)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                val position = list.size
                list.add(it)
                adapter.notifyItemInserted(position)
                binding.rvBackground.scrollToPosition(position)
            }
        }
    }
}

