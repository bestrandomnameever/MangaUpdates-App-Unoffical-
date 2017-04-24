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
import android.util.Log
import android.view.View
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_mangaDetail.MangaDetailActivity
import com.mangaupdates.android.mangaupdates_app_unofficial.models.EndlessRecyclerOnScrollListener
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MANGACOVERSPACING
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MINMANGACOVERWIDTH
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Call

open abstract class MangaCoverOverviewFragment : Fragment(), MangaCoversAdapter.MangaCoversAdapterListener {

    lateinit var recyclerview: RecyclerView
    lateinit var mangaCoversAdapter: MangaCoversAdapter
    lateinit var dividerItemDecoration: RecyclerView.ItemDecoration
    lateinit var layoutManager: GridLayoutManager
    lateinit var endlessLoadingScrollListener: EndlessRecyclerOnScrollListener
    private var page = 1

    private var snackBarShown = false
    val mangaQueue = PublishSubject.create<String>()
    private val pendingLoading = mutableListOf<Disposable>()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idealColumns = getIdealAmountOfColumns()
        layoutManager = GridLayoutManager(context, idealColumns)
        mangaCoversAdapter = MangaCoversAdapter(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = mangaCoversAdapter

        endlessLoadingScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun loadMore() {
                page += 1
                loadIds(page)
            }
        }
        recyclerview.addOnScrollListener(endlessLoadingScrollListener)

        dividerItemDecoration = GridSpacingItemDecoration(idealColumns, MANGACOVERSPACING, true)
        recyclerview.addItemDecoration(dividerItemDecoration)
        loadIds(page)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        pendingLoading.forEach {
            disp -> disp.dispose()
        }
        super.onDetach()
        //TODO fix java.io.InterruptedIOException when disposing calls
    }

    fun reload() {
        page = 1
        mangaCoversAdapter.reset()
        endlessLoadingScrollListener.reset()
        loadIds(page)
    }

    open abstract fun setLoadingMethod(forPage: Int): Call<ResponseBody>

    fun loadIds(forPage: Int) {
        val call = setLoadingMethod(forPage)
        pendingLoading.add(MangaUpdatesAPI.getIdsFromCallAsync(call).subscribeBy(
                onNext = { result ->
                    if (!result.hasNextPage) {
                        endlessLoadingScrollListener.allPagesLoaded = true
                    }
                    //offset for index of current ids already loaded
                    val currentAmountOfIds = mangaCoversAdapter.itemCount

                    //fill recyclerview with empty entrys for each gotten id and update adapter for loading covers
                    result.ids.forEachIndexed { index, _ -> mangaCoversAdapter.mangas.put(currentAmountOfIds + index, null) }
                    mangaCoversAdapter.notifyDataSetChanged()
                    endlessLoadingScrollListener.loading = false

                    //load manga for each id
                    pendingLoading.add(MangaUpdatesAPI.getMangasWithIdsAsync(ids = result.ids, offset = currentAmountOfIds).subscribeBy(
                            onNext = {manga -> if (manga != null) insertManga(manga.index, manga.value)},
                            onError = {error -> Log.d("KAPOTE", error.message)},
                            onComplete = {}
                        )
                    )
                },
                onError = {error -> error.printStackTrace()}
        )
        )
    }

    private fun insertManga(index: Int, manga: MangaDetail) {
        mangaCoversAdapter.mangas.replace(index, manga)
        mangaCoversAdapter.notifyItemChanged(index)
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
            val snackbar = Snackbar.make(recyclerview, "No internet connection", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("RETRY", View.OnClickListener {
                snackbar.dismiss()
                snackBarShown
                reload()
            })
            snackbar.show()
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}

/*
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
import android.util.Log
import android.view.View
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_mangaDetail.MangaDetailActivity
import com.mangaupdates.android.mangaupdates_app_unofficial.models.EndlessRecyclerOnScrollListener
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MANGACOVERSPACING
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MINMANGACOVERWIDTH
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Call

open abstract class MangaCoverOverviewFragment : Fragment(), MangaCoversAdapter.MangaCoversAdapterListener {

    lateinit var recyclerview: RecyclerView
    lateinit var mangaCoversAdapter: MangaCoversAdapter
    lateinit var dividerItemDecoration: RecyclerView.ItemDecoration
    lateinit var layoutManager: GridLayoutManager
    lateinit var endlessLoadingScrollListener: EndlessRecyclerOnScrollListener
    private var page = 1

    private var snackBarShown = false
    val mangaQueue = PublishSubject.create<IndexedValue<String>>()
    private val pendingLoading = mutableListOf<Disposable>()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idealColumns = getIdealAmountOfColumns()
        layoutManager = GridLayoutManager(context, idealColumns)
        mangaCoversAdapter = MangaCoversAdapter(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = mangaCoversAdapter

        endlessLoadingScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun loadMore() {
                page += 1
                loadIds(page)
            }
        }
        recyclerview.addOnScrollListener(endlessLoadingScrollListener)

        dividerItemDecoration = GridSpacingItemDecoration(idealColumns, MANGACOVERSPACING, true)
        recyclerview.addItemDecoration(dividerItemDecoration)
        initMangaLoading()
        loadIds(page)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        pendingLoading.forEach {
            disp -> disp.dispose()
        }
        super.onDetach()
        //TODO fix java.io.InterruptedIOException when disposing calls
    }

    fun reload() {
        page = 1
        mangaCoversAdapter.reset()
        endlessLoadingScrollListener.reset()
        pendingLoading.forEach { disp -> disp.dispose() }
        initMangaLoading()
        loadIds(page)
    }

    open abstract fun setLoadingMethod(forPage: Int): Call<ResponseBody>

    fun initMangaLoading () {
        pendingLoading.add(MangaUpdatesAPI.getMangasWithIdsAsync(ids = mangaQueue).subscribeBy(
                onNext = {manga -> if (manga != null) insertManga(manga.index, manga.value)},
                onError = {error -> Log.d("KAPOTE", error.message)},
                onComplete = {}
        )
        )
    }

    fun loadIds(forPage: Int) {
        val call = setLoadingMethod(forPage)
        MangaUpdatesAPI.getIdsFromCallAsync(call).subscribeBy(
                onNext = { result ->
                    if (!result.hasNextPage) {
                        endlessLoadingScrollListener.allPagesLoaded = true
                    }
                    //offset for index of current ids already loaded
                    val currentAmountOfIds = mangaCoversAdapter.itemCount

                    //fill recyclerview with empty entrys for each gotten id and update adapter for loading covers
                    result.ids.forEachIndexed { index, _ -> mangaCoversAdapter.mangas.put(currentAmountOfIds + index, null) }
                    mangaCoversAdapter.notifyDataSetChanged()
                    endlessLoadingScrollListener.loading = false

                    //load manga for each id
                    result.ids.forEachIndexed { index, id ->
                        mangaQueue.onNext(IndexedValue(currentAmountOfIds + index, id))
                    }
                },
                onError = {error -> error.printStackTrace()}
        )
    }

    private fun insertManga(index: Int, manga: MangaDetail) {
        mangaCoversAdapter.mangas.replace(index, manga)
        mangaCoversAdapter.notifyItemChanged(index)
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
            val snackbar = Snackbar.make(recyclerview, "No internet connection", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("RETRY", View.OnClickListener {
                snackbar.dismiss()
                snackBarShown
                reload()
            })
            snackbar.show()
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}*/
