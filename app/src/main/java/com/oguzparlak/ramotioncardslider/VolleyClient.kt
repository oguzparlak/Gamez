package com.oguzparlak.ramotioncardslider

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.oguzparlak.ramotioncardslider.helper.interfaces.Error
import com.oguzparlak.ramotioncardslider.helper.interfaces.HttpClient
import org.greenrobot.eventbus.EventBus

class VolleyClient private constructor() {

    private var network = BasicNetwork(HurlStack())

    private var requestQueue: RequestQueue = RequestQueue(NoCache(), network)

    private lateinit var httpClient: HttpClient

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

    private object Holder { val INSTANCE = VolleyClient() }

    fun setHttpClient(httpClient: HttpClient) {
        this.httpClient = httpClient
    }

    fun makeRequest(method: Int = Request.Method.GET,
                            headers: MutableMap<String, String>? = mutableMapOf("Client-ID" to "euf4aa5zzjyq07ypuhivsn920p41in"),
                            url: String): JsonObjectRequest {
        return object: JsonObjectRequest(method, url, null,
                Response.Listener {
                    response ->
                        httpClient.onResponseReceived(response)
                },
                Response.ErrorListener { error -> httpClient.onError(Error(error.networkResponse.statusCode, error.message)) }){
                    override fun getHeaders(): MutableMap<String, String> {
                        return headers ?: super.getHeaders()
                    }
                }
    }

    /*
    fun addToRequestQueue(url: String, headers: MutableMap<String, String>? = null) {
        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(NoCache(), network).apply {
            start()
            add(makeRequest(url, headers))
        }
    }
    */

}