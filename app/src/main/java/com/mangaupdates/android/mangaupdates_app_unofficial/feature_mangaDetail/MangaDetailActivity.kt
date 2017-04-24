package com.mangaupdates.android.mangaupdates_app_unofficial.feature_mangaDetail

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_discover.MangaDetailPageAdapter
import kotlinx.android.synthetic.main.app_bar_manga_detail.*

class MangaDetailActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var viewpager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mangaId = intent.extras.getString("mangaId")
        setContentView(R.layout.app_bar_manga_detail)

        setTitle(R.string.mangaDetailActivityName)

        toolbar = findViewById(R.id.mangaDetailToolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewpager = this.mangaDetailViewPager
        tabLayout = this.mangaDetailTabs
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewpager.setCurrentItem(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewpager.adapter = MangaDetailPageAdapter(supportFragmentManager)
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun getMangaFromId() {

    }

}
