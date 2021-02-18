package com.shishiapp.playerdemo

import com.shishiapp.playerdemo.util.Constants
import okhttp3.HttpUrl

fun Long.toDurationString(): String {
    val secs: Long = this / 1000

    if(secs / 3600 >= 1) {
        return String.format("%d:%02d:%02d", secs / 3600, secs % 3600 / 60, secs % 60)
    } else {
        return String.format("%d:%02d", secs % 3600 / 60, secs % 60)
    }
}

fun String.getMediaUrl(): String {
    return HttpUrl.parse(Constants.baseUrl)!!.newBuilder()
        .addEncodedPathSegments(this.removePrefix("/"))
        .addQueryParameter("X-Plex-Token", Constants.token).build().toString()
}
