package com.mangaupdates.android.mangaupdates_app_unofficial.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.LOADMORETHRESHOLD
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaAdapterLoader
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.releases_mangacover_viewholder.view.*

/**
 * Created by Anthony on 13/03/2017.
 */
class MangaCoversAdapter(listener: MangaCoversAdapterListener, loader: MangaAdapterLoader): Adapter<MangaCoversAdapter.ReleaseCoverViewHolder>() {

    val mangas = mutableMapOf<Int, MangaDetail?>()
    val pendingLoading = mutableListOf<Disposable>()

    val listener: MangaCoversAdapterListener = listener
    val loader = loader

    var progressBar: ProgressBar? = null

    var page = 1
    var loading = false
    var allPagesLoaded = false

    init {
        loadIds(page)
    }

    constructor(listener: MangaCoversAdapterListener, loader: MangaAdapterLoader, progressBar: ProgressBar) : this(listener, loader) {
        this.progressBar = progressBar
    }

    override fun onBindViewHolder(holder: ReleaseCoverViewHolder?, position: Int) {
        if (holder != null) {
            val manga = mangas[position]

            if(manga != null) {

                holder.setChapter(manga.latestChapter)
                holder.setTitle(manga.name)
                holder.setUrlImage(manga.imageUrl)
                holder.itemView.mangaReleaseLayout.setOnClickListener { listener.openMangaDetailWithId(manga.id)}
                holder.setIsLoaded()
            }else {
                holder.setIsLoading()
            }

            if(position > mangas.size - LOADMORETHRESHOLD && canLoadMoreIds()){
                page++
                loadIds(page)
            }
        }
    }

    override fun getItemCount(): Int {
        return mangas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReleaseCoverViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.releases_mangacover_viewholder, parent, false)
        return ReleaseCoverViewHolder(view)
    }

    class ReleaseCoverViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun setUrlImage(url: String) {
            itemView.coverImageView.loadUrl(url)
        }

        fun setChapter(text: String) {
            itemView.chapterNumberTextView.text = text
        }

        fun setTitle(text: String) {
            itemView.coverMangaNameTextView.text = text
        }

        fun setIsLoading() {
            itemView.loadingLayout.visibility = View.VISIBLE
        }

        fun setIsLoaded() {
            itemView.loadingLayout.visibility = View.INVISIBLE
        }
    }

    fun loadIds(forPage: Int) {
        if(canLoadMoreIds()) {
            loading = true

            val call = loader.getIdsForPage(forPage)
            pendingLoading.add(MangaUpdatesAPI.getIdsFromCallAsync(call).subscribeBy(
                    onNext = { result ->
                        if (!result.hasNextPage) {
                            allPagesLoaded = true
                        }
                        //offset for index of current ids already loaded
                        val currentAmountOfIds = itemCount

                        //fill recyclerview with empty entrys for each gotten id and update adapter for loading covers
                        result.ids.forEachIndexed { index, _ -> mangas.put(currentAmountOfIds + index, null) }
                        notifyDataSetChanged()
                        loading = false

                        //load manga for each id
                        pendingLoading.add(MangaUpdatesAPI.getMangasWithIdsAsync(ids = result.ids, offset = currentAmountOfIds).subscribeBy(
                                onNext = {manga -> if (manga != null) insertManga(manga.index, manga.value)},
                                onError = {error -> Log.d("KAPOTE", error.message)},
                                onComplete = {}
                        )
                        )
                    },
                    onError = {error -> error.printStackTrace()},
                    onComplete = { loading = false }
            )
            )
        }
    }

    private fun insertManga(index: Int, manga: MangaDetail) {
        mangas.replace(index, manga)
        notifyItemChanged(index)
    }

    private fun canLoadMoreIds() : Boolean {
        return !loading && !allPagesLoaded
    }

    fun reset() {
        mangas.clear()
        page = 1
        notifyDataSetChanged()
        loadIds(page)
    }

    fun close() {
        pendingLoading.forEach { disp -> disp.dispose() }
    }

    interface MangaCoversAdapterListener {
        fun openMangaDetailWithId(id: String)
    }
}
