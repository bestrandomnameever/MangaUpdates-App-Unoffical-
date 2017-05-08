package com.mangaupdates.android.mangaupdates_app_unofficial.feature.discover

import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate

/**
 * Created by Anthony on 19/03/2017.
 */
class SeriesStatsFragment : android.support.v4.app.Fragment() {

    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        var view = container!!.inflate(com.mangaupdates.android.mangaupdates_app_unofficial.R.layout.fragment_series_stats)
        return view
    }


    companion object {
        fun newInstance() : android.support.v4.app.Fragment {
            return com.mangaupdates.android.mangaupdates_app_unofficial.feature.discover.SeriesStatsFragment()
        }
    }
}