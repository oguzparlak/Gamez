package com.oguzparlak.gamez

import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NoCache
import com.google.gson.Gson
import com.oguzparlak.gamez.model.Game
import com.oguzparlak.gamez.model.StreamFactory
import com.oguzparlak.gamez.model.StreamType
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class VolleyClient private constructor() {

    private var network = BasicNetwork(HurlStack())

    private var requestQueue: RequestQueue = RequestQueue(NoCache(), network)

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

    private fun getJsonObjectRequest(method: Int = Request.Method.GET,
                                     headers: MutableMap<String, String>? = mutableMapOf("Client-ID" to "euf4aa5zzjyq07ypuhivsn920p41in"),
                                     url: String,
                                     successListener: Response.Listener<JSONObject>? = null,
                                     errorListener: Response.ErrorListener? = null): JsonObjectRequest {
        return object: JsonObjectRequest(method, url, null, successListener, errorListener) {
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
     * Retrieves the streams
     * Request Method: GET
     * @param url: Url of the endpoint
     */
    fun getStreams(streamType: StreamType, url: String) {
        addToRequestQueue(getJsonObjectRequest(url = url,
                errorListener = Response.ErrorListener { error ->
                    EventBus.getDefault().post(error)
        },
                successListener =  Response.Listener { response ->
                    val responseHandler = streamType.getResponseHandler(response.getRoot())
                    val streams = responseHandler?.handle()
                    val streamMessage = StreamFactory.getStreamMessage(streamType, streams!!)
                    EventBus.getDefault().post(streamMessage)
        }))
    }

    fun getTopGames(url: String) {
        addToRequestQueue(getJsonObjectRequest(url = url,
                errorListener = Response.ErrorListener { error ->
                    EventBus.getDefault().post(error)
                },
                successListener = Response.Listener { response ->
                    val root = response.getRoot()
                    val topGames = root.asJsonObject["top"].asJsonArray
                    val games = topGames.map { Gson().fromJson(it, Game::class.java) }
                    EventBus.getDefault().post(games)
                }))
    }

    /**
     * Adds the given request to the queue
     */
    private fun addToRequestQueue(request: JsonObjectRequest) {
        requestQueue.add(request)
    }

}