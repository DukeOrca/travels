package com.duke.orca.android.kotlin.travels.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.scale(scale: Float, duration: Number = 200) {
    this.animate()
        .scaleX(scale)
        .scaleY(scale)
        .setDuration(duration.toLong())
        .start()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide(invisible: Boolean = false) {
    if (invisible)
        this.visibility = View.INVISIBLE
    else
        this.visibility = View.GONE
}

fun View.fadeIn(duration: Number) {
    this.apply {
        alpha = 0F
        visibility = View.VISIBLE

        animate()
            .alpha(1F)
            .setDuration(duration.toLong())
            .setListener(null)
    }
}

fun View.fadeOut(duration: Number) {
    this.apply {
        alpha = 1F
        visibility = View.VISIBLE

        animate()
            .alpha(0F)
            .setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    this@fadeOut.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            })
    }
}
