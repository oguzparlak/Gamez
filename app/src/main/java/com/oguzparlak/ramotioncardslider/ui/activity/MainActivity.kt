package com.oguzparlak.ramotioncardslider.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.oguzparlak.ramotioncardslider.service.PopularStreamsAsyncJob
import com.oguzparlak.ramotioncardslider.R
import com.oguzparlak.ramotioncardslider.helper.interfaces.TwitchStreamClient
import com.oguzparlak.ramotioncardslider.helper.querybuilder.FeaturedStreamsQuery
import com.oguzparlak.ramotioncardslider.helper.querybuilder.FeaturedStreamsQueryBuilder
import com.oguzparlak.ramotioncardslider.helper.querybuilder.StreamQuery
import com.oguzparlak.ramotioncardslider.helper.querybuilder.StreamQueryBuilder
import com.oguzparlak.ramotioncardslider.ui.fragment.FeaturedStreamFragment
import com.oguzparlak.ramotioncardslider.ui.fragment.StreamFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mBottomNavigationView.setOnNavigationItemSelectedListener(this)
        openFragment()
        PopularStreamsAsyncJob.scheduleJob()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val menuId = item!!.itemId
        when (menuId) {
            R.id.action_search -> {
                // TODO Implement it later
                return true
            }
            R.id.action_filter -> {
                // TODO Implement it later
                return true
            }
        }
        return false
    }

    private fun openFragment() {
        // Add Fragment
        val streamFragment = StreamFragment()
        val featuredStreamsFragment = FeaturedStreamFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.mFragmentContainer, streamFragment)
                .commit()

        supportFragmentManager.beginTransaction()
                .replace(R.id.mSecondFragmentContainer, featuredStreamsFragment)
                .commit()

        TwitchStreamClient(StreamQueryBuilder()
                .getQuery(StreamQuery(limit = "100")))
                .makeRequest()

        TwitchStreamClient(FeaturedStreamsQueryBuilder()
                .getQuery(FeaturedStreamsQuery()))
                .makeRequest()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_stream -> openFragment()
            R.id.action_games -> print("")
            R.id.action_favs -> print("")
        }
        return true
    }


}
