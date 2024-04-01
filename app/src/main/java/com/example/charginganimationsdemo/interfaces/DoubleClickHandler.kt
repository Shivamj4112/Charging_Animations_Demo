package com.example.charginganimationsdemo.interfaces

class DoubleClickHandler(private val listener: OnDoubleClickListener) {

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Time between double clicks in milliseconds
    }

    private var lastClickTime: Long = 0

    fun handleClick() {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            listener.onDoubleClick()
        }
        lastClickTime = clickTime
    }
}
