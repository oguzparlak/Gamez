package com.oguzparlak.ramotioncardslider.helper.querybuilder

/**
 * Get any Stream
 */
data class StreamQuery(
        val channelId: String? = null, /* Get specific stream via channel_id */
        val streamType: String? = null, /* Default live */
        val limit: String? = null, /* Default 25, max: 100 */
        val game: String? = null,
        val language: String? = null, /* Default: all languages */
        val offset: String? = null /* Default 0 */
)

/**
 * Get summary of a Stream
 */
data class StreamSummaryQuery(
        val game: String? = null
)

/**
 * Get featured Streams
 */
data class FeaturedStreamsQuery(
        val limit: String? = null,
        val offset: String? = null
)

/**
 * Get Streams followed by a user
 */
data class FollowedStreamQuery(
        val streamType: String? = null,
        val limit: String? = null,
        val offset: String? = null
)

data class UserStreamQuery(
        val channelId: String,
        val streamType: String? = null
)