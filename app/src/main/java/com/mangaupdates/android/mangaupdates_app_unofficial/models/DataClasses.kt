package com.mangaupdates.android.mangaupdates_app_unofficial.models

/**
 * Created by Anthony on 17/03/2017.
 */
/////////////////
//MangaUpdates
/////////////////
data class MangaUpdatesResult(val ids: List<String>, val hasNextPage: Boolean)

data class MangaDetail(val id: String, val name: String, val imageUrl: String, val latestChapter: String)

/////////////////
//MangaCoversDB
/////////////////

data class MangaCoverDBDetail (val title: String, val alternativeTitles: Collection<String>, val artists: Collection<String>, val authors: Collection<String>,
                               val releaseYear: String, val type: String , val tags: Collection<String> ,val statusTags: MangaCoverDBDetailStatus, val covers : Collection<MangaCoverDBDetailCover>)

data class MangaCoverDBDetailStatus(val completed: Boolean, val ecchi: Boolean, val hentai: Boolean, val mature: Boolean, val yaoi: Boolean, val yuri: Boolean)

data class MangaCoverDBDetailCover(val mIME: String, val normal: String , val raw: String , val side: String, val thumbnail: String, val volume: Int)