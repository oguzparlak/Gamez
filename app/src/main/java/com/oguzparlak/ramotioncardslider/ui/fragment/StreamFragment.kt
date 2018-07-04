package com.oguzparlak.ramotioncardslider.ui.fragment

import com.oguzparlak.ramotioncardslider.model.LiveStreamMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StreamFragment : BaseStreamFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(streamMessage: LiveStreamMessage) {
        updateDisplay(streamMessage.streams)
    }

    override val title: String
        get() = "Top Streams"

}
