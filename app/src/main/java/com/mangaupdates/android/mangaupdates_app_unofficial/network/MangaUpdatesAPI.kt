package com.mangaupdates.android.mangaupdates_app_unofficial.network

import com.anupcowkur.reservoir.Reservoir
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MAXMANGALOADINGTHREADS
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaUpdatesHTMLParser
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaUpdatesResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by Anthony on 16/03/2017.
 */
object MangaUpdatesAPI {

    val retrofit = Retrofit.Builder().baseUrl("https://www.mangaupdates.com/").client(OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.SECONDS)
            .readTimeout(500, TimeUnit.SECONDS)
            .writeTimeout(500, TimeUnit.SECONDS)
            .build()
    ).build()
    val mangaUpdatesService = retrofit.create(MangaUpdateService::class.java)


    //GET IDS

    fun getReleasesForPage(page: Int): Call<ResponseBody> {
        return mangaUpdatesService.getReleases(page.toString())
    }

    fun getIdsWhatsNewPage(orderBy: String, type: String, year: String, page: Int): Call<ResponseBody> {
        return mangaUpdatesService.getSeriesWhatsNewPage(orderBy, type, year, page, 75)
    }

    fun getIdsFromCallAsync(call: Call<ResponseBody>): Observable<MangaUpdatesResult> {
        return Observable.just(call)
                .subscribeOn(Schedulers.io())
                .map { call -> call.execute().body() }
                .subscribeOn(Schedulers.newThread())
                .map { responseBody -> MangaUpdatesHTMLParser.getIds(responseBody.string()) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    //GET MANGA

    private fun getMangaWithIdCall(id: String): Call<ResponseBody> {
        return mangaUpdatesService.getMangaWithId(id)
    }

    fun getMangaWithIdAsync(id: String): Observable<MangaDetail> {
        return Observable.just(id)
                .subscribeOn(Schedulers.io())
                .map { id -> if (!Reservoir.contains(id)) MangaUpdatesHTMLParser.loadMangaFromHtml(MangaUpdatesAPI.getMangaWithIdCall(id).execute().body().string(), id) else Reservoir.get(id, MangaDetail::class.java) }
                .doOnEach { manga -> if (!Reservoir.contains(manga.value.id)) Reservoir.put(manga.value.id, manga.value) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMangasWithIdsAsync(ids: List<String>, offset: Int): Observable<IndexedValue<MangaDetail>>  {
        val indexedIds = ids.withIndex()
        return indexedIds.toObservable()
                .map { idAndIndex -> IndexedValue(idAndIndex.index + offset, idAndIndex.value) }
                .concatMapEager({ value ->
                    Observable.just(value)
                            .subscribeOn(Schedulers.io())
                            .map { indexedValue ->
                                if (!Reservoir.contains(indexedValue.value)) {
                                    IndexedValue(indexedValue.index, MangaUpdatesHTMLParser.loadMangaFromHtml(MangaUpdatesAPI.getMangaWithIdCall(indexedValue.value).execute().body().string(), indexedValue.value))
                                } else {
                                    IndexedValue(indexedValue.index, Reservoir.get(indexedValue.value, MangaDetail::class.java))
                                }
                            }
                }, MAXMANGALOADINGTHREADS, MAXMANGALOADINGTHREADS)
                .doOnEach { manga ->
                    if (!Reservoir.contains(manga.value.value.id)) Reservoir.put(manga.value.value.id, manga.value.value)
                }.observeOn(AndroidSchedulers.mainThread())
    }
}

/*
package com.mangaupdates.android.mangaupdates_app_unofficial.network

import com.anupcowkur.reservoir.Reservoir
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MAXMANGALOADINGTHREADS
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaUpdatesHTMLParser
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaUpdatesResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

*/
/**
 * Created by Anthony on 16/03/2017.
 *//*

object MangaUpdatesAPI {

    val retrofit = Retrofit.Builder().baseUrl("https://www.mangaupdates.com/").client(OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.SECONDS)
            .readTimeout(500, TimeUnit.SECONDS)
            .writeTimeout(500, TimeUnit.SECONDS)
            .build()
    ).build()
    val mangaUpdatesService = retrofit.create(MangaUpdateService::class.java)


    //GET IDS

    fun getReleasesForPage(page: Int): Call<ResponseBody> {
        return mangaUpdatesService.getReleases(page.toString())
    }

    fun getIdsWhatsNewPage(orderBy: String, type: String, year: String, page: Int): Call<ResponseBody> {
        return mangaUpdatesService.getSeriesWhatsNewPage(orderBy, type, year, page, 75)
    }

    fun getIdsFromCallAsync(call: Call<ResponseBody>): Observable<MangaUpdatesResult> {
        return Observable.just(call)
                .subscribeOn(Schedulers.io())
                .map { call -> call.execute().body() }
                .subscribeOn(Schedulers.newThread())
                .map { responseBody -> MangaUpdatesHTMLParser.getIds(responseBody.string()) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    //GET MANGA

    private fun getMangaWithIdCall(id: String): Call<ResponseBody> {
        return mangaUpdatesService.getMangaWithId(id)
    }

    fun getMangaWithIdAsync(id: String): Observable<MangaDetail> {
        return Observable.just(id)
                .subscribeOn(Schedulers.io())
                .map { id -> if (!Reservoir.contains(id)) MangaUpdatesHTMLParser.loadMangaFromHtml(MangaUpdatesAPI.getMangaWithIdCall(id).execute().body().string(), id) else Reservoir.get(id, MangaDetail::class.java) }
                .doOnEach { manga -> if (!Reservoir.contains(manga.value.id)) Reservoir.put(manga.value.id, manga.value) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    */
/*fun getMangasWithIdsAsync(ids: List<String>, offset: Int): Observable<IndexedValue<MangaDetail>>  {
        val indexedIds = ids.withIndex()
        return indexedIds.toObservable()
                .map { idAndIndex -> IndexedValue(idAndIndex.index + offset, idAndIndex.value) }
                .flatMap({ value ->
                    Observable.just(value)
                            .subscribeOn(Schedulers.io())
                            .map { indexedValue ->
                                if (!Reservoir.contains(indexedValue.value)) {
                                    IndexedValue(indexedValue.index, MangaUpdatesHTMLParser.loadMangaFromHtml(MangaUpdatesAPI.getMangaWithIdCall(indexedValue.value).execute().body().string(), indexedValue.value))
                                } else {
                                    IndexedValue(indexedValue.index, Reservoir.get(indexedValue.value, MangaDetail::class.java))
                                }
                            }
                }, MAXMANGALOADINGTHREADS)
                .doOnEach { manga ->
                    if (!Reservoir.contains(manga.value.value.id)) Reservoir.put(manga.value.value.id, manga.value.value)
                }.observeOn(AndroidSchedulers.mainThread())
    }*//*


    fun getMangasWithIdsAsync(ids: Observable<IndexedValue<String>>): Observable<IndexedValue<MangaDetail>>  {
        return ids
                .concatMapEager({ value ->
                    Observable.just(value)
                            .subscribeOn(Schedulers.io())
                            .map { indexedValue ->
                                if (!Reservoir.contains(indexedValue.value)) {
                                    IndexedValue(indexedValue.index, MangaUpdatesHTMLParser.loadMangaFromHtml(MangaUpdatesAPI.getMangaWithIdCall(indexedValue.value).execute().body().string(), indexedValue.value))
                                } else {
                                    IndexedValue(indexedValue.index, Reservoir.get(indexedValue.value, MangaDetail::class.java))
                                }
                            }
                }, MAXMANGALOADINGTHREADS, MAXMANGALOADINGTHREADS)
                .doOnEach { manga ->
                    if (!Reservoir.contains(manga.value.value.id)) Reservoir.put(manga.value.value.id, manga.value.value)
                }.observeOn(AndroidSchedulers.mainThread())
    }
}*/
