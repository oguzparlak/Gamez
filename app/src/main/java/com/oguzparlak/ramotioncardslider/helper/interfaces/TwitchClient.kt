package com.oguzparlak.ramotioncardslider.helper.interfaces

import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.VolleyClient
import com.oguzparlak.ramotioncardslider.getResponseHandler
import com.oguzparlak.ramotioncardslider.hide
import com.oguzparlak.ramotioncardslider.model.Stream
import com.oguzparlak.ramotioncardslider.model.StreamType
import com.oguzparlak.ramotioncardslider.ui.fragment.StreamFragment
import kotlinx.android.synthetic.main.stream_fragment.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class TwitchClient(private val url: String) : HttpClient {

    private val volleyClient: VolleyClient = VolleyClient.instance

    private var streamType = StreamType.AllStreams

    init {
        // Set HttpClient
        volleyClient.setHttpClient(this)
    }

    fun makeRequest() {
        volleyClient.addToRequestQueue(volleyClient.makeRequest(url = url))
    }

    override fun onResponseReceived(response: JSONObject) {
        val root = JsonParser().parse(response.toString())
        if (response.has("featured")) streamType = StreamType.FeaturedStreamType
        val responseHandler = streamType.getResponseHandler(root)
        val streams = responseHandler?.handle()
        EventBus.getDefault().post(streams)
    }

    override fun onResponseReceived(response: JSONArray) {
        // TODO Implement it later
    }

    override fun onError(error: Error) {
        EventBus.getDefault().post(error)
    }

}