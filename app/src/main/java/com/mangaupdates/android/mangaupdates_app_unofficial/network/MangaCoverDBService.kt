package com.mangaupdates.android.mangaupdates_app_unofficial.network

import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Anthony on 18/03/2017.
 */
interface MangaCoverDBService {
    @GET("api/v1/series/{MUid}/")
    fun getMangaWithId(@Path("MUid") MUid: String) : Call<MangaCoverDBDetail>
}
