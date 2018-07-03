package com.oguzparlak.ramotioncardslider.helper.interfaces

import com.oguzparlak.ramotioncardslider.VolleyClient
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class TwitchClient<T>(private val url: String) : HttpClient {

    init {
        // Set HttpClient
        VolleyClient.instance.setHttpClient(this)
    }

    fun makeRequest() {
        VolleyClient.instance.makeRequest(url = url)
    }

    override fun onResponseReceived(response: JSONObject) {
        EventBus.getDefault().post(response)
    }

    override fun onResponseReceived(response: JSONArray) {

    }

    override fun onError(error: Error) {

    }

}