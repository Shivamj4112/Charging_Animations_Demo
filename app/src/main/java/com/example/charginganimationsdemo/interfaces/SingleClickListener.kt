package com.example.charginganimationsdemo.interfaces

import android.os.SystemClock
import android.view.View


abstract class SingleClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(view: View?)

    override fun onClick(view: View) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - mLastClickTime > MIN_CLICK_INTERVAL) {
            mLastClickTime = currentTime
            onSingleClick(view)
        }
    }

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 1000
    }
}


//abstract class SingleClickListener : View.OnClickListener {
//    private var mLastClickTime: Long = 0
//    abstract fun onSingleClick(view: View?)
//    override fun onClick(view: View) {
//        val uptimeMillis = SystemClock.uptimeMillis()
//        mLastClickTime = uptimeMillis
//        if (uptimeMillis - uptimeMillis > 1000) {
//            onSingleClick(view)
//        }
//    }
//
//    companion object {
//        private const val MIN_CLICK_INTERVAL: Long = 1000
//    }
//}
