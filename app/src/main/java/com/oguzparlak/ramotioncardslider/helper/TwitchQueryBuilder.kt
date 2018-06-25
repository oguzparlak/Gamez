package com.oguzparlak.ramotioncardslider.helper

import com.oguzparlak.ramotioncardslider.addQueryParameter

/**
 * Gets a list of all featured live streams.
 */
class FeaturedStreamsQueryBuilder: QueryBuilder<FeaturedStreamsQuery>("https://api.twitch.tv/kraken/streams/featured/") {

    override fun getQuery(query: FeaturedStreamsQuery): String {
        val builder = baseUri.buildUpon()
                .addQueryParameter("limit", query.limit)
                .addQueryParameter("offset", query.offset)
        return builder.build().toString()
    }

}

/**
 * Gets a list of live(or any) streams.
 */
class StreamQueryBuilder: QueryBuilder<StreamQuery>("https://api.twitch.tv/kraken/streams/") {

    override fun getQuery(query: StreamQuery): String {
        val builder = baseUri.buildUpon()
                .addQueryParameter("stream_type", query.streamType)
                .addQueryParameter("limit", query.limit)
                .addQueryParameter("channel_id", query.channelId)
                .addQueryParameter("offset", query.offset)
                .addQueryParameter("game", query.game)
                .addQueryParameter("language", query.language)
        return builder.build().toString()
    }

}

/**
 * Gets a summary of live streams.
 */
class StreamSummaryQueryBuilder: QueryBuilder<StreamSummaryQuery>("https://api.twitch.tv/kraken/streams/summary/") {

    override fun getQuery(query: StreamSummaryQuery): String {
        val builder = baseUri.buildUpon()
                .addQueryParameter("game", query.game)
        return builder.build().toString()
    }

}

/**
 * Gets a list of online streams a user is following, based on a specified OAuth token.
 * Required Scope: user_read
 * Must send an OAuth token in the header
 * like this 'Authorization: OAuth cfabdegwdoklmawdzdo98xt2fo512y'
 */
class FollowedStreamsQueryBuilder: QueryBuilder<FollowedStreamQuery>("https://api.twitch.tv/kraken/streams/followed/") {

    override fun getQuery(query: FollowedStreamQuery): String {
        val builder = baseUri.buildUpon()
                .addQueryParameter("stream_type", query.streamType)
                .addQueryParameter("limit", query.limit)
                .addQueryParameter("offset", query.offset)
        return builder.build().toString()
    }

}

/**
 * Gets stream information (the stream object) for a specified user.
 */
class UserStreamQueryBuilder: QueryBuilder<UserStreamQuery>("https://api.twitch.tv/kraken/streams/") {

    override fun getQuery(query: UserStreamQuery): String {
        val builder = baseUri.buildUpon()
                .appendPath(query.channelId)
                .addQueryParameter("stream_type", query.streamType)
        return builder.build().toString()
    }

}