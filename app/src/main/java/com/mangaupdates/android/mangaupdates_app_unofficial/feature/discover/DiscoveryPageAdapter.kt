package com.mangaupdates.android.mangaupdates_app_unofficial.feature.discover

/**
 * Created by Anthony on 19/03/2017.
 */

class DiscoveryPageAdapter(fm: android.support.v4.app.FragmentManager) : android.support.v4.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): android.support.v4.app.Fragment {
        when (position) {
            0 -> return WhatsnewFragement.newInstance()
            else -> return SeriesStatsFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }

}
