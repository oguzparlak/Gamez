package com.oguzparlak.ramotioncardslider.ui.fragment

import com.oguzparlak.ramotioncardslider.model.FeaturedStreamMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FeaturedStreamFragment: BaseStreamFragment() {

    /**
     * The response from EventBus will be handled
     * in this block
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(streamMessage: FeaturedStreamMessage) {
        updateDisplay(streamMessage.streams)
    }

    override val title: String
        get() = "Featured Streams"

}