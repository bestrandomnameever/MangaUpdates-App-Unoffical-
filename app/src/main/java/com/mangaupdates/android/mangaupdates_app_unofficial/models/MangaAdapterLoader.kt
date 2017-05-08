package com.mangaupdates.android.mangaupdates_app_unofficial.models

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Created by Anthony on 8/05/2017.
 */
interface MangaAdapterLoader {
    fun getIdsForPage(page: Int) : Call<ResponseBody>
}