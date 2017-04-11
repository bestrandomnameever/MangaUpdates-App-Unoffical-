package com.mangaupdates.android.mangaupdates_app_unofficial.models

import com.anupcowkur.reservoir.Reservoir
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import com.mangaupdates.android.mangaupdates_app_unofficial.views.adapters.MangaCoversAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Anthony on 4/04/2017.
 */
open abstract class MangaCoverLoader(adapter: MangaCoversAdapter, scrollListener: EndlessRecyclerOnScrollListener) {
    val mangaCoversAdapter = adapter
    val endlessScrollListener = scrollListener

    /*fun makeCallForIds(forPage: Int) : Call<ResponseBody> {
        return MangaUpdatesAPI.getReleasesForPage(forPage)
    }*/
    open abstract fun makeCallForIds(forPage: Int) : Call<ResponseBody>

    fun loadIds(forPage: Int) {
        /*MangaUpdatesAPI.getReleasesForPage(forPage , object : retrofit2.Callback<ResponseBody> {*/
        makeCallForIds(forPage).enqueue( object : retrofit2.Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response!!.isSuccessful) {
                    val result = MangaUpdatesHTMLParser.getIds(response.body().string())
                    if (!result.hasNextPage) {
                        endlessScrollListener.allPagesLoaded = true
                    }

                    //offset for index of current ids already loaded
                    val currentAmountOfIds = mangaCoversAdapter.itemCount - 1
                    //fill recyclerview with empty entrys for each gotten id and update adapter for loading covers
                    result.ids.forEachIndexed { index, _ ->  mangaCoversAdapter.mangas.put(currentAmountOfIds + index, null) }
                    mangaCoversAdapter.notifyDataSetChanged()

                    //load manga for each id
                    result.ids.forEachIndexed { index, id ->
                        //check cache for manga with id
                        if(Reservoir.contains(id)) {
                            var manga = Reservoir.get(id, MangaDetail::class.java)
                            insertManga(index, manga)
                        }else {
                            loadMangaFor(id, currentAmountOfIds+index)
                        }
                    }
                    endlessScrollListener.loading = false
                }else {
                    print("oeps")
                }
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun loadMangaFor(id: String, index: Int) {
        MangaUpdatesAPI.getMangaWithId(id, object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response!!.isSuccessful) {
                    val manga = MangaUpdatesHTMLParser.loadMangaFromHtml(response.body().string(), id)
                    Reservoir.put(manga.id, manga)
                    insertManga(index, manga)
                }else {
                    print("Loading manga failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun insertManga(index: Int, manga: MangaDetail) {
        mangaCoversAdapter.mangas.replace(index, manga)
        mangaCoversAdapter.notifyItemChanged(index)
    }
}