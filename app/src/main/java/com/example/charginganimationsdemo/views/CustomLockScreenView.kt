package com.example.charginganimationsdemo.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.charginganimationsdemo.R
import com.example.charginganimationsdemo.interfaces.OnSingleClickListener

class CustomLockScreenView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var clickListener: OnSingleClickListener? = null

    fun setClick(listener: OnSingleClickListener) {
        clickListener = listener
    }
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_lock_screen, this, true)
//        val singleClick = findViewById<ImageView>(R.id.iv_close)
//
//        setOnClickListener {
//
//            singleClick.visibility = View.VISIBLE
//            Handler().postDelayed({singleClick.visibility = View.INVISIBLE},2000)
//
//        }
        findViewById<RelativeLayout>(R.id.main)?.setOnClickListener {
            clickListener?.onSingleClick()
        }

    }
}
