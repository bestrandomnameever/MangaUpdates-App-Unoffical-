package com.mangaupdates.android.mangaupdates_app_unofficial.`feature_discover`

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_mangaDetail.MangaDetailFragment

/**
 * Created by Anthony on 19/03/2017.
 */

class MangaDetailPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return MangaDetailFragment.newInstance()
            1 -> return MangaDetailFragment.newInstance()
            2 -> return MangaDetailFragment.newInstance()
            3 -> return MangaDetailFragment.newInstance()
            else -> return MangaDetailFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 5

    }

}
