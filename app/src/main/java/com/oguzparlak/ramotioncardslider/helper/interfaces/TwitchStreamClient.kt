package com.oguzparlak.ramotioncardslider.helper.interfaces

import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.VolleyClient
import com.oguzparlak.ramotioncardslider.getResponseHandler
import com.oguzparlak.ramotioncardslider.model.StreamFactory
import com.oguzparlak.ramotioncardslider.model.StreamType
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class TwitchStreamClient(private val url: String) : HttpClient {

    private val volleyClient: VolleyClient = VolleyClient.instance

    private lateinit var streamType: StreamType

    init {
        // Set HttpClient
        volleyClient.setHttpClient(this)
    }

    fun makeRequest() {
        volleyClient.addToRequestQueue(volleyClient.makeRequest(url = url))
    }

    override fun onResponseReceived(response: JSONObject) {
        val root = JsonParser().parse(response.toString())
        streamType = getStreamType(response)
        val responseHandler = streamType.getResponseHandler(root)
        val streams = responseHandler?.handle()
        val streamMessage = StreamFactory.getStreamMessage(streamType, streams!!)
        EventBus.getDefault().post(streamMessage)
    }

    private fun getStreamType(response: JSONObject): StreamType {
        return if (response.has("featured"))
            StreamType.FeaturedStreamType
        else
            StreamType.AllStreams
    }

    override fun onResponseReceived(response: JSONArray) {
        // TODO Implement it later
    }

    override fun onError(error: Error) {
        EventBus.getDefault().post(error)
    }

}