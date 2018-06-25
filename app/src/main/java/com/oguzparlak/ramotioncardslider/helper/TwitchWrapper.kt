package com.oguzparlak.ramotioncardslider.helper

import android.net.Uri

import com.oguzparlak.ramotioncardslider.model.Stream

/**
 * Created by vb53347 on 25.06.2018.
 */

class TwitchWrapper {

    /**
     * Builds a Stream URL for endpoint: https://api.twitch.tv/kraken/streams/
     * with specified parameters or default if non is specified
     */
    fun buildStreamUrl(query: StreamQuery): String {
        val uri = Uri.parse(BASE_URL)
        val builder = uri.buildUpon()
                .addQueryParameter("stream_type", query.streamType)
                .addQueryParameter("limit", query.limit)
        return builder.build().toString()
    }

    private fun Uri.Builder.addQueryParameter(key: String?, value: String?): Uri.Builder {
        if (key == null || value == null) return this
        return this.appendQueryParameter(key, value)
    }

    companion object {
        private val BASE_URL = "https://api.twitch.tv/kraken/streams/"
    }
}
