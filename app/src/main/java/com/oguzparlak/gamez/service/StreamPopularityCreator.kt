package com.oguzparlak.gamez.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.text.Html
import android.widget.Toast
import androidx.core.content.edit
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.oguzparlak.gamez.R
import com.oguzparlak.gamez.VolleyClient
import com.oguzparlak.gamez.appInForeground
import com.oguzparlak.gamez.model.Game
import com.oguzparlak.gamez.toK
import com.oguzparlak.gamez.ui.activity.MainActivity
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.net.URLDecoder
import java.util.concurrent.TimeUnit


class StreamPopularityCreator : JobCreator {

    override fun create(tag: String): Job? {
        return PopularStreamsAsyncJob()
    }

}

class PopularStreamsAsyncJob : Job() {

    companion object {

        private const val GAME_NOTIFICATION = 1000

        private const val GAME_KEY = "game_key"

        private const val TAG = "PopularStreamsAsyncJob"

        private const val CHANNEL_ID = "com.oguzparlak.gamez.populargames"

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
        // Get top Games from Twitch
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gameId = sharedPreferences.getLong(GAME_KEY, -1)
        // Read the previous game id if two ids are the same then return
        if (gameId == mostPopularGame!!.gameDetail.id) {
            return
        }
        // Write the Game Id into Shared Preferences
        sharedPreferences.edit {
            putLong(GAME_KEY, mostPopularGame.gameDetail.id)
        }
        val textContent = String.format("%s has reached %s viewer count",
                mostPopularGame.gameDetail.name, mostPopularGame.viewerCount.toK())
        // If app is in the foreground show a Toast message to user
        if (context.appInForeground()) {
            // TODO Not working fix this
            Toasty.info(context, textContent, Toast.LENGTH_LONG, true).show();
        } else {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Set tap action of the notification
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            // Build the notification
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_games_24dp)
                    .setContentTitle(String.format("%s is on fire !! ${generateFireEmoji()}",
                            mostPopularGame.gameDetail.name))
                    .setContentText(textContent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(textContent))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build()
            // If the os version is higher than 26, create a notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Popular Games Alert"
                val description = "Alerts you when a game's view count is higher related to others"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                notificationManager.createNotificationChannel(channel)
            }

            // Show the notification
            notificationManager.notify(GAME_NOTIFICATION, notification)
        }
    }

    private fun generateFireEmoji() : String {
        return Html.fromHtml(URLDecoder.decode("%f0%9f%94%a5")).toString()
    }

    // TODO Change the type of Error
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onError(error: Error) {
        // Log.d(TAG, "onMessageReceived: error: $error")
    }

}