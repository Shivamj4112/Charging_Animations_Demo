package com.example.charginganimationsdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.charginganimationsdemo.databinding.LayoutItemBinding

class ItemAdapter(var onClick: (Int) -> Unit) : RecyclerView.Adapter<ItemAdapter.DataHolder>() {

    private lateinit var list: List<Int>
    private lateinit var context: Context

    class DataHolder(itemView: LayoutItemBinding) : ViewHolder(itemView.root) {
        var binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        context = parent.context
        var binding = LayoutItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return DataHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        holder.binding.apply {

            animationView.setAnimation(list[position])

            root.setOnClickListener {
                onClick.invoke(list[position])
                //                imgDone.visibility = View.VISIBLE
            }
        }
    }

    fun updateData(anim: List<Int>) {
        list = anim
    }
}