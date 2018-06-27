package com.oguzparlak.ramotioncardslider.helper

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.oguzparlak.ramotioncardslider.model.Stream

/**
 * Created by vb53347 on 27.06.2018.
 */

class FeaturedStreamsResponseHandler(rootElement: JsonElement) : JsonResponseHandler<Stream>(rootElement) {

    override val rootArray: JsonArray
        get() = rootElement.asJsonObject["featured"].asJsonArray

    override fun handle(): List<Stream> {
        return rootArray
                .map { it.asJsonObject["stream"].asJsonObject }
                .map { Gson().fromJson(it, Stream::class.java) }
    }
}
