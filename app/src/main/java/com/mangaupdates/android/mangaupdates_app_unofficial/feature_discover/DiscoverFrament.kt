package com.mangaupdates.android.mangaupdates_app_unofficial.`feature_discover`

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import kotlinx.android.synthetic.main.fragment_discover_frament.*
import kotlinx.android.synthetic.main.fragment_discover_frament.view.*

class DiscoverFrament : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.title = "Discover"
        val view =  inflater!!.inflate(R.layout.fragment_discover_frament, container, false)
        val tabLayout =  view.discoverTabLayout
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                discoverPager.setCurrentItem(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        val pager = view.discoverPager
        pager.adapter = DiscoveryPageAdapter(fragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        fun newInstance(): DiscoverFrament {
            val fragment = DiscoverFrament()
            return fragment
        }
    }
}
