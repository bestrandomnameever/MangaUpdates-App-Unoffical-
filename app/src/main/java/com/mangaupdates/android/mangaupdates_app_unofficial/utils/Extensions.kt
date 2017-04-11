package com.mangaupdates.android.mangaupdates_app_unofficial.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverDBDetail

/**
 * Created by Anthony on 14/03/2017.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).placeholder(R.drawable.imageunavailable1).error(R.drawable.imageunavailable2).into(this)
}

fun MangaCoverDBDetail.findThumbnailUrlForVolume(volume: Int): String{
    val cover = this.covers.filter { e -> e.volume==volume && e.side.equals("front") }.first()
    if (!cover.thumbnail.isEmpty()) {
        return cover.thumbnail
    }else if(!cover.normal.isEmpty()) {
        return cover.normal
    }else {
        return cover.raw
    }
}
