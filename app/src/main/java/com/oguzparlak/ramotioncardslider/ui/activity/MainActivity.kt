package com.oguzparlak.ramotioncardslider.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.oguzparlak.ramotioncardslider.R
import com.oguzparlak.ramotioncardslider.ui.fragment.GameFragment
import com.oguzparlak.ramotioncardslider.ui.fragment.StreamFragment
import kotlinx.android.synthetic.main.activity_main.*

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

    fun openFragment() {
        // Add Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.mFragmentContainer, StreamFragment())
                .commit()

        supportFragmentManager.beginTransaction()
                .replace(R.id.mSecondFragmentContainer, StreamFragment())
                .commit()
    }

    fun openAnotherFragment() {
        // Add Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.mFragmentContainer, GameFragment())
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_stream -> openFragment()
            R.id.action_games -> openAnotherFragment()
            R.id.action_favs -> print("")
        }
        return true
    }


}
