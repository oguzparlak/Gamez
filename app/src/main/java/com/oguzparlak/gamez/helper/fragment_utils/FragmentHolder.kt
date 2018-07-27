package com.oguzparlak.gamez.helper.fragment_utils

import android.support.v4.app.Fragment
import java.util.*

class FragmentHolder(val tabCount: Int) {

    val fragmentStack: Stack<Fragment> = Stack()

}

class FragmentRouter {

    private lateinit var fragmentDictionaries: List<HashMap<Int, FragmentHolder>>

    private var activeIndex = 0

    /**
     * Pushes a Fragment to specified index
     */
    fun addFragmentTo(index: Int, fragment: Fragment, fromScratch: Boolean = false) {

    }

    /**
     * Kills the current fragment,
     * at the current index
     */
    fun killCurrentFragment() {
        val fragmentStack = fragmentDictionaries[activeIndex][activeIndex]?.fragmentStack
        if (fragmentStack!!.any()) fragmentStack.pop()

    }

    fun selectActiveIndex(index: Int) {
        val fragment = fragmentDictionaries[index][index]!!.fragmentStack.peek()

    }

    fun handleDeepLink() {

    }

}