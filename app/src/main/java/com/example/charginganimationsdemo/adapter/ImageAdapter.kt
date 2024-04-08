package com.example.charginganimationsdemo.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.charginganimationsdemo.databinding.CustomWallppaperItemBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.Dataholder>() {

    private lateinit var context : Context
    private var list =  ArrayList<Uri>()
    class Dataholder(itemView: CustomWallppaperItemBinding) : ViewHolder(itemView.root){
        var binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dataholder {
        context = parent.context
        var binding = CustomWallppaperItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return Dataholder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Dataholder, position: Int) {

        holder.binding.apply {

            imgAnimation.setImageURI(list[position])
        }

    }

    fun updateImage(list: ArrayList<Uri>) {
        this.list = list
    }
}