package com.oguzparlak.ramotioncardslider.model

import com.google.gson.annotations.SerializedName

data class Stream(
        @SerializedName("_id")
        val id: Long,

        @SerializedName("viewers")
        val viewerCount: Long,

        @SerializedName("stream_type")
        val streamType: String,

        val game: String,

        val preview: Preview,

        val channel: Channel
)

data class Preview(val small: String, val medium: String, val large: String, val template: String)

data class Channel(
        @SerializedName("_id")
        val id: Long,

        val description: String,

        @SerializedName("mature")
        val isMature: Boolean,

        val status: String,

        @SerializedName("broadcaster_language")
        val language: String,

        @SerializedName("display_name")
        val displayName: String,

        val logo: String,

        @SerializedName("video_banner")
        val videoBanner: String,

        @SerializedName("profile_banner")
        val profileBanner: String,

        val followers: Long
)

data class StreamSummary(
        @SerializedName("channels")
        val channelCount: Long,

        @SerializedName("viewers")
        val viewerCount: Long
)