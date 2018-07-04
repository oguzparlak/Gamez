package com.oguzparlak.ramotioncardslider.helper.interfaces

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by vb53347 on 3.07.2018.
 */
interface HttpClient {
    fun onResponseReceived(response: JSONObject)
    fun onResponseReceived(response: JSONArray)
    fun onError(error: Error)
}

data class Error(val statusCode: Int?, val message: String?)