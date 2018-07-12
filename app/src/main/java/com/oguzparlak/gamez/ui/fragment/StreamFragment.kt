package com.oguzparlak.gamez.ui.fragment

import com.oguzparlak.gamez.R
import com.oguzparlak.gamez.model.LiveStreamMessage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StreamFragment : BaseStreamFragment() {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageReceived(streamMessage: LiveStreamMessage) {
        updateDisplay(streamMessage.streams)
    }

    override val title: String
        get() = getString(R.string.top_streams)

}
