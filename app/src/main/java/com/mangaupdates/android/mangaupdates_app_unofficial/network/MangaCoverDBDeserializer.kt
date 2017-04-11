package com.mangaupdates.android.mangaupdates_app_unofficial.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetail
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetailCover
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetailStatus
import java.lang.reflect.Type

/**
 * Created by Anthony on 19/03/2017.
 */
class MangaCoverDBDeserializer : JsonDeserializer<MangaCoverDBDetail> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MangaCoverDBDetail {
        val jsonObject = json!!.asJsonObject
        val alternateTitles = jsonObject.getAsJsonArray("AlternativeTitles").map { e -> e.asString }
        val artists = jsonObject.getAsJsonArray("Artists").map { e -> e.asString }
        val authors = jsonObject.getAsJsonArray("Authors").map { e -> e.asString }
        val releaseYear = jsonObject.get("ReleaseYear").asString
        val title = jsonObject.get("Title").asString
        val type = jsonObject.get("Type").asString
        val tags = jsonObject.getAsJsonArray("Tags").map { e -> e.asString }
        /*val volumes = jsonObject.get("Volumes").asInt
        val volumesAvailable = jsonObject.get("VolumesAvailable").asInt*/

        val statusTagsObject = jsonObject.getAsJsonObject("StatusTags")
        val completed = statusTagsObject.get("Completed").asBoolean
        val ecchi = statusTagsObject.get("Ecchi").asBoolean
        val hentai = statusTagsObject.get("Hentai").asBoolean
        val mature = statusTagsObject.get("Mature").asBoolean
        val yaoi = statusTagsObject.get("Yaoi").asBoolean
        val yuri = statusTagsObject.get("Yuri").asBoolean
        val statusTags = MangaCoverDBDetailStatus(completed,ecchi,hentai,mature, yaoi,yuri)

        val coversObjectArray = jsonObject.getAsJsonObject("Covers").getAsJsonArray("a")
        var covers : MutableList<MangaCoverDBDetailCover> = mutableListOf()
        coversObjectArray.forEach { e ->
            val coverObject = e.asJsonObject
            val mIME = coverObject.get("MIME").asString
            var normal = ""
            if (coverObject.get("Normal") != null) {
                normal = coverObject.get("Normal").asString
            }
            val raw = coverObject.get("Raw").asString
            val side = coverObject.get("Side").asString
            var thumbnail = ""
            if (coverObject.get("Thumbnail") != null) {
                thumbnail = coverObject.get("Thumbnail").asString
            }
            val volume = coverObject.get("Volume").asInt
            covers.add(MangaCoverDBDetailCover(mIME, normal, raw, side, thumbnail, volume))
        }
        return MangaCoverDBDetail(title,alternateTitles,artists, authors, releaseYear, type, tags, statusTags, covers)
    }

}