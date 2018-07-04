package com.oguzparlak.ramotioncardslider.service

import android.support.v4.app.NotificationCompat
import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.oguzparlak.ramotioncardslider.R
import com.oguzparlak.ramotioncardslider.VolleyClient
import com.oguzparlak.ramotioncardslider.helper.interfaces.TwitchGameClient
import com.oguzparlak.ramotioncardslider.model.Game
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import android.support.v4.app.NotificationManagerCompat



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
                    // .startNow()
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
        TwitchGameClient(TOP_GAMES_ENDPOINT).makeRequest()
        return Result.SUCCESS
    }

    override fun onCancel() {
        super.onCancel()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageReceived(games: List<Game>) {
        // Get max view count from games
        val mostPopularGame = games.maxBy { it.viewerCount }
        // TODO Save the game in SharedPreferences
        // TODO Prepare and show notification
        val mBuilder = NotificationCompat.Builder(context, "Media & Entertainment")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("Woooohooo")
                .setContentText(mostPopularGame!!.gameDetail.name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onError(error: Error) {
        Log.d(TAG, "onMessageReceived: error: $error")
    }

}