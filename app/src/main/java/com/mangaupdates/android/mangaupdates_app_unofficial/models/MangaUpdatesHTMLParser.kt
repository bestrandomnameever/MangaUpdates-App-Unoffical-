package com.mangaupdates.android.mangaupdates_app_unofficial.models

import org.jsoup.Jsoup

/**
 * Created by Anthony on 18/03/2017.
 */
class MangaUpdatesHTMLParser {

    companion object {
        //Works for releases, whats new, search results
        fun getIds (htmlString : String) : MangaUpdatesResult{
            val document = Jsoup.parse(htmlString)
            val ids = document.body().select("[title~=Series]").map { result -> result.attr("href").split("id=")[1] }
            val hasNextPage = document.body().select("a:containsOwn(Next Page)").size > 0
            return MangaUpdatesResult(ids = ids, hasNextPage = hasNextPage)
        }

        fun loadMangaFromHtml (htmlString : String, id: String) : MangaDetail {
            val document = Jsoup.parse(htmlString)
            val title = document.body().select("span.releasestitle").text()
            var imageUrl = ""
            var description = ""
            var type = ""
            var latestChapter = ""
            var statusCountryOrigin = ""
            var completelyScanlated = ""

            document.body().select(".sCat").forEach {
                e ->
                when (e.select("b").text().toLowerCase()) {
                    "image" -> {
                        imageUrl = e.nextElementSibling().select("img").attr("src")
                    }

                    "description" -> {
                        description = e.parent().select(".sContent").text()
                    }
                    "type" -> {
                        type = e.parent().select(".sContent").text()
                    }
                    "latest release(s)" -> {
                        val chapter = e.nextElementSibling().text().split("by ")[0].split("c.").last().trim()
                        if(chapter.isEmpty()) {
                            latestChapter = "-"
                        }else {
                            latestChapter = chapter
                        }
                    }

                }
            }
            return MangaDetail(id,title,imageUrl,latestChapter)
        }
    }
}