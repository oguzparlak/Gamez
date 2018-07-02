package com.oguzparlak.ramotioncardslider.service

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.oguzparlak.ramotioncardslider.VolleyClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class StreamPopularityCreator : JobCreator {

    override fun create(tag: String): Job? {
        return PopularStreamsAsyncJob()
    }

}

class PopularStreamsAsyncJob : Job() {

    companion object {
        private const val TAG = "PopularStreamsAsyncJob"

        const val TOP_GAMES_ENDPOINT = "https://api.twitch.tv/kraken/games/top"

        fun scheduleJob(extras: PersistableBundleCompat? = null) {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setUpdateCurrent(true)
                    .setExtras(extras)
                    .build()
                    .schedule()
        }

    }

    override fun onRunJob(params: Params): Result {
        // This job will show a notification if a game's viewer count
        // exceeds 100K people.
        // Send a request to get the top games from TwitchAPI
        EventBus.getDefault().register(this)
        val volleyClient = VolleyClient.instance
        // TODO Change Client-ID redeclaration
        // TODO !! Important Change EventBus data handling to Object Oriented Metaphor, otherwise conflicts would occur
        // TODO Add Google Play Services Support https://github.com/evernote/android-job
        volleyClient.addToRequestQueue(TOP_GAMES_ENDPOINT, headers = mutableMapOf("Client-ID" to "euf4aa5zzjyq07ypuhivsn920p41in"))
        return Result.SUCCESS
    }

    override fun onCancel() {
        super.onCancel()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageReceived(data: Any) {
        Log.d(TAG, "onMessageReceived: ")
    }

}