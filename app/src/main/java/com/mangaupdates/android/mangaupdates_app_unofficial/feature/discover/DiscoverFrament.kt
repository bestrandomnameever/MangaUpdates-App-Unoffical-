package com.mangaupdates.android.mangaupdates_app_unofficial.feature.discover

import kotlinx.android.synthetic.main.fragment_discover_frament.view.*

class DiscoverFrament : android.support.v4.app.Fragment() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?,
                              savedInstanceState: android.os.Bundle?): android.view.View? {
        activity.title = "Discover"
        val view =  inflater!!.inflate(com.mangaupdates.android.mangaupdates_app_unofficial.R.layout.fragment_discover_frament, container, false)
        val tabLayout = view.discoverTabLayout
        tabLayout.addOnTabSelectedListener(object: android.support.design.widget.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: android.support.design.widget.TabLayout.Tab?) {
                view.discoverPager.setCurrentItem(tab!!.position)
            }

            override fun onTabUnselected(tab: android.support.design.widget.TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: android.support.design.widget.TabLayout.Tab?) {
            }
        })
        val pager = view.discoverPager
        pager.adapter = DiscoveryPageAdapter(fragmentManager)
        pager.addOnPageChangeListener(android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        return view
    }

    override fun onAttach(context: android.content.Context?) {
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
