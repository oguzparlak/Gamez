package com.oguzparlak.ramotioncardslider

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat


/**
 * Extensions will be live here
 */

/**
 * Returns the width of the frame
 */
fun Activity.getFrameWidth() : Int {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

/**
 * Hides the soft keyboard
 */
fun Context.hideKeyboardFrom(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Shows the soft keyboard
 */
fun Context.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Applies fadeIn animation to view
 */
fun View.applyFadeInAnimation(context: Context, fillAfter: Boolean = false) {
    val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    fadeIn.fillAfter = fillAfter
    this.startAnimation(fadeIn)
}

/**
 * Applies fadeOut animation to view
 */
fun View.applyFadeOutAnimation(context: Context, fillAfter: Boolean = false) {
    val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    fadeIn.fillAfter = fillAfter
    this.startAnimation(fadeIn)
}

fun View.hide(shouldGone: Boolean = false) {
    visibility = if (shouldGone)
        View.GONE
    else
        View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Turns this: 2454,
 * to this: 2.4K
 */
fun Long.toK(): String {
    val stringBuilder = StringBuilder()
    val firstDigit: Double = this.toDouble() / 1000
    val decimalFormat = DecimalFormat("#.#")
    val formattedString = decimalFormat.format(firstDigit)
    return stringBuilder.append(formattedString)
            .append("K").toString()
}

fun Uri.Builder.addQueryParameter(key: String?, value: String?): Uri.Builder {
    if (key == null || value == null) return this
    return this.appendQueryParameter(key, value)
}