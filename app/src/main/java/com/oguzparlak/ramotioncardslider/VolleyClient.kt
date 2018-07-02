package com.oguzparlak.ramotioncardslider

import android.app.Activity
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.greenrobot.eventbus.EventBus

class VolleyClient private constructor() {

    private lateinit var requestQueue: RequestQueue

    /**
     * A flag that indicates whether VolleyClient is prepared
     */
    private var prepared = false

    companion object {

        /**
         * Log TAG
         */
        private const val TAG = "VolleyClient"

        /**
         * Singleton Instance
         */
        val instance: VolleyClient by lazy { Holder.INSTANCE }

    }

    fun prepareWithContext(activity: Activity) {
        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        val cache = DiskBasedCache(activity.cacheDir, 1024 * 1024)

        requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        prepared = true
    }

    private object Holder { val INSTANCE = VolleyClient() }

    private fun makeRequest(url: String, headers: MutableMap<String, String>? = null): JsonObjectRequest {
        return object: JsonObjectRequest(Method.GET, url, null,
                Response.Listener {
                    response ->
                    // Send message via EventBus
                    EventBus.getDefault().post(response)
                },
                Response.ErrorListener { error -> Log.e(TAG, "error: $error")}){
                    override fun getHeaders(): MutableMap<String, String> {
                        return headers ?: super.getHeaders()
                    }
                }
    }

    fun addToRequestQueue(url: String, headers: MutableMap<String, String>? = null) {
        if (!prepared) throw IllegalArgumentException("VolleyClient should be prepared before making a call")
        requestQueue.add(makeRequest(url, headers))
    }

}