package com.mangaupdates.android.mangaupdates_app_unofficial.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Anthony on 16/03/2017.
 */
interface MangaUpdateService {
    @GET("releases.html")
    fun getReleases(@Query("page") page: String) : Call<ResponseBody>

    @GET("series.html")
    fun getMangaWithId(@Query("id") id: String) : Call<ResponseBody>

    @GET("series.html")
    fun getMangaWithIdObservable(@Query("id") id: String) : Observable<ResponseBody>

    @GET("series.html")
    fun searchMangaWithTitle(@Query("search") title: String, @Query("page") page: Int) : Call<ResponseBody>

    @GET("https://www.mangaupdates.com/whatsnew.html")
    fun getSeriesWhatsNewPage(@Query("orderby") orderby : String, @Query("type") type: String, @Query("year") year: String, @Query("page") page: Int, @Query("perpage") perpage: Int) : Call<ResponseBody>
}