package com.oguzparlak.ramotioncardslider.service

import android.annotation.TargetApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.oguzparlak.ramotioncardslider.VolleyClient
import com.oguzparlak.ramotioncardslider.model.Game
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import com.oguzparlak.ramotioncardslider.R
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import com.oguzparlak.ramotioncardslider.ui.activity.MainActivity


class StreamPopularityCreator : JobCreator {

    override fun create(tag: String): Job? {
        return PopularStreamsAsyncJob()
    }

}

class PopularStreamsAsyncJob : Job() {

    companion object {
        private const val TAG = "PopularStreamsAsyncJob"

        private const val CHANNEL_ID = "com.oguzparlak.tweech.populargames"

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
        VolleyClient.instance.getTopGames(TOP_GAMES_ENDPOINT)
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
        // Log.d(TAG, "Most popular game is ${mostPopularGame!!.gameDetail.name}")
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val mBuilder = NotificationCompat.Builder(context, "Media & Entertainment")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("Woooohooo")
                .setContentIntent(pendingIntent)
                .setContentText(mostPopularGame!!.gameDetail.name)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(0x001, mBuilder.build())
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Popular Games"// context.getString(R.string.channel_name)
        val description = "Get notified when a game's view count is so high" // context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onError(error: Error) {
        // Log.d(TAG, "onMessageReceived: error: $error")
    }

}