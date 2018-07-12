package com.oguzparlak.gamez.ui.fragment

import com.oguzparlak.gamez.R
import com.oguzparlak.gamez.model.FeaturedStreamMessage
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
        get() = getString(R.string.featured_streams)

}