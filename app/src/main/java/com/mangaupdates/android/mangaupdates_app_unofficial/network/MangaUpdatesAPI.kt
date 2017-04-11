package com.mangaupdates.android.mangaupdates_app_unofficial.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

/**
 * Created by Anthony on 16/03/2017.
 */
object MangaUpdatesAPI {

    val retrofit = Retrofit.Builder().baseUrl("https://www.mangaupdates.com/").build()
    val mangaUpdatesService = retrofit.create(MangaUpdateService::class.java)


    //GET IDS

    fun getReleasesForPage(page: Int, callback: Callback<ResponseBody>) {
        mangaUpdatesService.getReleases(page.toString()).enqueue(callback)
    }

    fun getReleasesForPage(page: Int) : Call<ResponseBody>{
        return mangaUpdatesService.getReleases(page.toString())
    }

    fun getIdsWhatsNewPage(orderBy: String, type: String, year: String, page: Int, callback: Callback<ResponseBody>) {
        mangaUpdatesService.getSeriesWhatsNewPage(orderBy, type, year, page, 50).enqueue(callback)
    }

    //GET MANGA

    fun getMangaCallsForIds(ids: List<String>): List<Call<ResponseBody>>{
        return ids.map { it -> mangaUpdatesService.getMangaWithId(it) }
    }

    fun getMangaWithId(id: String, callback: Callback<ResponseBody>) {
        mangaUpdatesService.getMangaWithId(id).enqueue(callback)
    }
}