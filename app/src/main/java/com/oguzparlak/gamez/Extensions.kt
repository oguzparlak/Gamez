package com.oguzparlak.gamez

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.support.v4.widget.NestedScrollView
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.oguzparlak.gamez.helper.responsehandler.FeaturedStreamsResponseHandler
import com.oguzparlak.gamez.helper.responsehandler.JsonResponseHandler
import com.oguzparlak.gamez.helper.responsehandler.StreamResponseHandler
import com.oguzparlak.gamez.model.Stream
import com.oguzparlak.gamez.model.StreamType
import org.json.JSONObject
import java.text.DecimalFormat
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.text.Html
import android.widget.ScrollView
import java.net.URLDecoder


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

/**
 * Appends a query parameter to uri, if the key and value are specified
 */
fun Uri.Builder.addQueryParameter(key: String?, value: String?): Uri.Builder {
    if (key == null || value == null) return this
    return this.appendQueryParameter(key, value)
}

/**
 * Returns a response handler with a provided root element
 */
fun StreamType.getResponseHandler(root: JsonElement) :  JsonResponseHandler<Stream>? {
    return when (this) {
        StreamType.FeaturedStreamType -> FeaturedStreamsResponseHandler(root)
        StreamType.AllStreams -> StreamResponseHandler(root)
    }
}

fun JSONObject.getRoot(): JsonElement {
    return JsonParser().parse(this.toString())
}

// TODO Fix ! Not working as expected
fun NestedScrollView.scrollTop() {
    this.post {
        // this.fling(0)
        this.smoothScrollTo(0, 0)
    }
}

/**
 * Checks whether the app is in the foreground
 */
fun Context.appInForeground(): Boolean {
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningAppProcesses = activityManager.runningAppProcesses ?: return false
    return runningAppProcesses.any { it.processName == this.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
}