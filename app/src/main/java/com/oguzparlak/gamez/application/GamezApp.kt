package com.oguzparlak.gamez.application

import android.app.Application
import com.evernote.android.job.JobManager
import com.oguzparlak.gamez.R
import com.oguzparlak.gamez.service.StreamPopularityCreator
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader


class GamezApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Evernote-Job
        JobManager.create(this).addJobCreator(StreamPopularityCreator())
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            BezierRadarHeader(context)
        }
    }

}