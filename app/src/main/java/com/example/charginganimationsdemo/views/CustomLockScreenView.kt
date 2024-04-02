package com.example.charginganimationsdemo.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.charginganimationsdemo.R

class CustomLockScreenView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    //    var clickListener: OnSingleClickListener? = null
//
//    fun setClick(listener: OnSingleClickListener) {
//        clickListener = listener
//    }
    init {

        LayoutInflater.from(context).inflate(R.layout.custom_lock_screen, this, true)
        val lottie = findViewById<LottieAnimationView>(R.id.animationView)
        val singleClick = findViewById<RelativeLayout>(R.id.view2)



        lottie.setAnimation(R.raw.anim20)

        singleClick.setOnClickListener {
            findViewById<ImageView>(R.id.iv_close).visibility = View.VISIBLE

            Handler().postDelayed({
                findViewById<ImageView>(R.id.iv_close).visibility = View.INVISIBLE
            }, 2000)
//            clickListener?.onSingleClick()
        }


    }


}
