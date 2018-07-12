package com.oguzparlak.gamez.model

import com.google.gson.annotations.SerializedName

data class Game(
        @SerializedName("channels")
        val channelCount: Long,

        @SerializedName("viewers")
        val viewerCount: Long,

        @SerializedName("game")
        val gameDetail: GameDetail

)

data class GameDetail(
        @SerializedName("_id")
        val id: Long,

        val box: Box,

        @SerializedName("giantbomb_id")
        val giantBombId: Long,

        val logo: Box,

        val name: String,

        val popularity: Long
)

data class Box(val large: String, val medium: String, val small: String)