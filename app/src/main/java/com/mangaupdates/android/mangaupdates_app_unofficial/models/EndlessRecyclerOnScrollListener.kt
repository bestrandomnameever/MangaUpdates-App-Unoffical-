package com.mangaupdates.android.mangaupdates_app_unofficial.models

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Anthony on 4/04/2017.
 */
open abstract class EndlessRecyclerOnScrollListener(layoutManager: GridLayoutManager): RecyclerView.OnScrollListener() {
    var loading = false
    var allPagesLoaded = false

    val layoutManager = layoutManager

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (!loading && totalItemCount <= lastVisibleItem + LOADMORETHRESHOLD && !allPagesLoaded && totalItemCount != 0) {
            loading = true
            loadMore()
        }
    }

    fun reset() {
        loading = false
        allPagesLoaded = false
    }

    open abstract fun loadMore()
}