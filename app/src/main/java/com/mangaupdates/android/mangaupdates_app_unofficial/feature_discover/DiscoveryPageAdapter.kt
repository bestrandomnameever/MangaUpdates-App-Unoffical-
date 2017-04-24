package com.mangaupdates.android.mangaupdates_app_unofficial.`feature_discover`

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Anthony on 19/03/2017.
 */

class DiscoveryPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return WhatsnewFragement.newInstance()
            else -> return SeriesStatsFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }

}
