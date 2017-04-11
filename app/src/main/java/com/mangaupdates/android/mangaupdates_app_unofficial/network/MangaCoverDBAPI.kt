package com.mangaupdates.android.mangaupdates_app_unofficial.network

import com.google.gson.GsonBuilder
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetail
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/**
 * Created by Anthony on 18/03/2017.
 */
object MangaCoverDBAPI {

    val gson = GsonBuilder().registerTypeAdapter(MangaCoverDBDetail::class.java, MangaCoverDBDeserializer()).create()
    var retrofit = Retrofit.Builder()
            .baseUrl("http://mcd.iosphe.re/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    val mangaCoverDBService = retrofit.create(MangaCoverDBService::class.java)

    fun getMangaWithId(id: String, callback: Callback<MangaCoverDBDetail>) {
        mangaCoverDBService.getMangaWithId(id).enqueue(callback)
    }
}