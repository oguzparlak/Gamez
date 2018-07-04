package com.oguzparlak.ramotioncardslider

import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.oguzparlak.ramotioncardslider.helper.interfaces.Error
import com.oguzparlak.ramotioncardslider.helper.interfaces.HttpClient

class VolleyClient private constructor() {

    private var network = BasicNetwork(HurlStack())

    private var requestQueue: RequestQueue = RequestQueue(NoCache(), network)

    private var httpClient: HttpClient? = null

    init {
        requestQueue.start()
    }

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

    // TODO Create a generic infrastructure, create separate methods and callbacks for each of them
    fun makeRequest(method: Int = Request.Method.GET,
                            headers: MutableMap<String, String>? = mutableMapOf("Client-ID" to "euf4aa5zzjyq07ypuhivsn920p41in"),
                            url: String): JsonObjectRequest {
        return object: JsonObjectRequest(method, url, null,
                Response.Listener {
                    response ->
                        httpClient?.onResponseReceived(response)
                },
                Response.ErrorListener { error -> httpClient?.onError(Error(error.networkResponse?.statusCode, error.message)) })
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        return headers ?: super.getHeaders()
                    }

                    /**
                     * 10 seconds timeout threshold, the default was 5
                     */
                    override fun getRetryPolicy(): RetryPolicy {
                        return DefaultRetryPolicy(10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                    }
                }
    }

    /**
     * Adds the given request to the queue
     */
    fun addToRequestQueue(request: JsonObjectRequest) {
        requestQueue.add(request)
    }

}