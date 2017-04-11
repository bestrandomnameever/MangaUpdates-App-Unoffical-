package com.mangaupdates.android.mangaupdates_app_unofficial.models

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Anthony on 4/04/2017.
 */
open abstract class EndlessRecyclerOnScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener() {
    var loading = true
    var lastVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var allPagesLoaded = false

    var currentPage = 1

    val layoutManager = layoutManager

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView!!.childCount
        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (!loading && totalItemCount <= lastVisibleItem + LOADMORETHRESHOLD && !allPagesLoaded) {
            loading = true
            currentPage += 1
            loadMore(currentPage)
        }
    }

    open abstract fun loadMore(currentPage: Int)
}