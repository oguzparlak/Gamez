package com.oguzparlak.gamez

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ResizeAnimation(internal var view: View, internal val targetWidth: Int) : Animation() {
    internal val startWidth: Int

    init {
        startWidth = view.width
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val newWidth = (startWidth + (targetWidth - startWidth) * interpolatedTime).toInt()
        view.layoutParams.width = newWidth
        view.requestLayout()
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}
