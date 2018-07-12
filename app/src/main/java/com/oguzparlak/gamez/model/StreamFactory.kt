package com.oguzparlak.gamez.model

class StreamFactory {

    companion object {
        fun getStreamMessage(streamType: StreamType, streams: List<Stream>): StreamMessage? {
            return when (streamType) {
                StreamType.AllStreams -> LiveStreamMessage(streams)
                StreamType.FeaturedStreamType -> FeaturedStreamMessage(streams)
            }
        }
    }

}

abstract class StreamMessage(val streams: List<Stream>) {

    internal abstract val streamType: StreamType

}

class LiveStreamMessage(streams: List<Stream>) : StreamMessage(streams) {

    override val streamType: StreamType
        get() = StreamType.AllStreams
}

class FeaturedStreamMessage(streams: List<Stream>) : StreamMessage(streams) {

    override val streamType: StreamType
        get() = StreamType.FeaturedStreamType
}
