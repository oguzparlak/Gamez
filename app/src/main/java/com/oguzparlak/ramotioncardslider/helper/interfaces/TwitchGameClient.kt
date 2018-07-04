package com.oguzparlak.ramotioncardslider.helper.interfaces

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.oguzparlak.ramotioncardslider.VolleyClient
import com.oguzparlak.ramotioncardslider.model.Game
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject

class TwitchGameClient(private val url: String): HttpClient {

    private val volleyClient = VolleyClient.instance

    init {
        volleyClient.setHttpClient(this)
    }

    fun makeRequest() {
        volleyClient.addToRequestQueue(volleyClient.makeRequest(url = url))
    }

    override fun onResponseReceived(response: JSONObject) {
        val root = JsonParser().parse(response.toString())
        val topGames = root.asJsonObject["top"].asJsonArray
        val games = topGames.map { Gson().fromJson(it, Game::class.java) }
        EventBus.getDefault().post(games)
    }

    override fun onResponseReceived(response: JSONArray) {

    }

    override fun onError(error: Error) {
        EventBus.getDefault().post(error)
    }

}