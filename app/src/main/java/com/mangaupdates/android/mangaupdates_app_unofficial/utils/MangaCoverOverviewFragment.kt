package com.mangaupdates.android.mangaupdates_app_unofficial.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_mangaDetail.MangaDetailActivity
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MANGACOVERSPACING
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MINMANGACOVERWIDTH
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaAdapterLoader

open abstract class MangaCoverOverviewFragment : Fragment(), MangaCoversAdapter.MangaCoversAdapterListener {

    lateinit var recyclerview: RecyclerView
    lateinit var mangaCoversAdapter: MangaCoversAdapter
    lateinit var dividerItemDecoration: RecyclerView.ItemDecoration
    lateinit var layoutManager: GridLayoutManager
    //lateinit var endlessLoadingScrollListener: EndlessRecyclerOnScrollListener

    private var snackBarShown = false

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idealColumns = getIdealAmountOfColumns()
        layoutManager = GridLayoutManager(context, idealColumns)
        recyclerview.layoutManager = layoutManager
        dividerItemDecoration = GridSpacingItemDecoration(idealColumns, MANGACOVERSPACING, true)
        recyclerview.addItemDecoration(dividerItemDecoration)

        /*endlessLoadingScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun loadMore() {
                mangaCoversAdapter.page++
                mangaCoversAdapter.loadIds(mangaCoversAdapter.page)
            }
        }
        recyclerview.addOnScrollListener(endlessLoadingScrollListener)*/


        mangaCoversAdapter = MangaCoversAdapter(this, getMangaAdapterLoader())
        recyclerview.adapter = mangaCoversAdapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        mangaCoversAdapter.close()
        super.onDetach()
        //TODO fix java.io.InterruptedIOException when disposing calls
    }

    fun reload() {
        //endlessLoadingScrollListener.reset()
        mangaCoversAdapter.reset()
    }

    override fun openMangaDetailWithId(id: String) {
        val intent = Intent(context, MangaDetailActivity::class.java)
        intent.putExtra("mangaId", id)
        context.startActivity(intent)
    }

    fun getIdealAmountOfColumns(): Int {
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        return (displayWidth - (displayWidth % MINMANGACOVERWIDTH)) / MINMANGACOVERWIDTH
    }

    private fun showReloadSnackbar() {
        if (!snackBarShown) {
            snackBarShown = true
            val snackbar = Snackbar.make(recyclerview, "No internet connection", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("RETRY", View.OnClickListener {
                snackbar.dismiss()
                snackBarShown = false
                reload()
            })
            snackbar.show()
        }
    }

    open abstract fun getMangaAdapterLoader() : MangaAdapterLoader

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
