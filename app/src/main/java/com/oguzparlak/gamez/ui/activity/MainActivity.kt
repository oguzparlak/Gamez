package com.oguzparlak.gamez.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import com.oguzparlak.gamez.BuildConfig
import com.oguzparlak.gamez.R
import com.oguzparlak.gamez.VolleyClient
import com.oguzparlak.gamez.helper.querybuilder.FeaturedStreamsQuery
import com.oguzparlak.gamez.helper.querybuilder.FeaturedStreamsQueryBuilder
import com.oguzparlak.gamez.helper.querybuilder.StreamQuery
import com.oguzparlak.gamez.helper.querybuilder.StreamQueryBuilder
import com.oguzparlak.gamez.model.StreamType
import com.oguzparlak.gamez.scrollTop
import com.oguzparlak.gamez.service.PopularStreamsAsyncJob
import com.oguzparlak.gamez.ui.fragment.FeaturedStreamFragment
import com.oguzparlak.gamez.ui.fragment.GameFragment
import com.oguzparlak.gamez.ui.fragment.StreamFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.SupportActivity

class MainActivity : SupportActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = this
    private val mNavigationItemReselectedListener: BottomNavigationView.OnNavigationItemReselectedListener = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        /*
        mBottomNavigationView.apply {
            setOnNavigationItemSelectedListener(mNavigationItemSelectedListener)
            setOnNavigationItemReselectedListener(mNavigationItemReselectedListener)
            onNavigationItemSelected(mBottomNavigationView.menu[0])
        }
        */
        PopularStreamsAsyncJob.scheduleJob()
        // TODO Change it according to tab index later
        mRefreshLayout.apply {
            setOnRefreshListener {
                openFragment()
                it.finishRefresh(480, true)
            }
            setOnLoadMoreListener {
                // do nothing
                it.finishLoadMoreWithNoMoreData()
            }
        }

        if (findFragment(StreamFragment::class.java) == null) {
            loadRootFragment(R.id.mFragmentContainer, StreamFragment())
        }

        val volleyClient = VolleyClient.instance
        volleyClient.getStreams(StreamType.AllStreams, StreamQueryBuilder().getQuery(StreamQuery(limit = "100")))
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

        val volleyClient = VolleyClient.instance
        volleyClient.getStreams(StreamType.FeaturedStreamType, FeaturedStreamsQueryBuilder().getQuery(FeaturedStreamsQuery()))
        volleyClient.getStreams(StreamType.AllStreams, StreamQueryBuilder().getQuery(StreamQuery(limit = "100")))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_stream -> {
                toolbar.title = getString(R.string.streams)
                openFragment()
                return true
            }
            R.id.action_games -> {
                toolbar.title = getString(R.string.games)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.mBaseFragmentContainer, GameFragment())
                        .commit()
                return true
            }
            R.id.action_favs -> {
                toolbar.title = getString(R.string.favorites)
                return true
            }
        }
        return false
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // TODO Implement it later
        // For now do nothing
        val id = item.itemId
        when (id) {
            R.id.action_stream -> mScrollView.scrollTop()
        }
    }


}
