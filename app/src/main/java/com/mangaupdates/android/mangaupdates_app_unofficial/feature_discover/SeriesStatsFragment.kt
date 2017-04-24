package com.mangaupdates.android.mangaupdates_app_unofficial.`feature_discover`

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate

/**
 * Created by Anthony on 19/03/2017.
 */
class SeriesStatsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = container!!.inflate(R.layout.fragment_series_stats)
        return view
    }


    companion object {
        fun newInstance() : Fragment {
            return SeriesStatsFragment()
        }
    }
}