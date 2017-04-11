package com.mangaupdates.android.mangaupdates_app_unofficial.views.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.loadUrl
import kotlinx.android.synthetic.main.releases_mangacover_viewholder.view.*

/**
 * Created by Anthony on 13/03/2017.
 */
class MangaCoversAdapter(listener: MangaCoversAdapterListener): Adapter<MangaCoversAdapter.ReleaseCoverViewHolder>() {

    val mangas = mutableMapOf<Int,MangaDetail?>()
    val listener: MangaCoversAdapterListener = listener
    var page = 1

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
            //Delete loading lkayout permanently
            //itemView.loadingLayout = null
        }
    }

    interface MangaCoversAdapterListener {
        fun openMangaDetailWithId(id: String)
    }
}
