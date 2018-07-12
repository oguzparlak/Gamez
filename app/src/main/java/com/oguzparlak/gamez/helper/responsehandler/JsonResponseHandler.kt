package com.oguzparlak.gamez.helper.responsehandler

import com.google.gson.JsonArray
import com.google.gson.JsonElement

/**
 * Created by vb53347 on 27.06.2018.
 */

abstract class JsonResponseHandler<out T>(val rootElement: JsonElement) {

    abstract val rootArray: JsonArray

    /**
     * Deserialize the given json input
     */
    abstract fun handle(): List<T>

}
