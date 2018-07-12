package com.oguzparlak.gamez.application

import android.app.Application
import com.evernote.android.job.JobManager
import com.oguzparlak.gamez.service.StreamPopularityCreator

class GamezApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(StreamPopularityCreator())
    }

}