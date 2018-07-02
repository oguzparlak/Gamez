package com.oguzparlak.ramotioncardslider.application

import android.app.Application
import com.evernote.android.job.JobManager
import com.oguzparlak.ramotioncardslider.service.StreamPopularityCreator

class TweechApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(StreamPopularityCreator())
    }

}