package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.model.Video
import io.realm.RealmModel

interface Repository {
    fun <T : RealmModel> get(
        path: String,
        type: Class<T>,
        success: (T) -> Unit,
        fail: (Error) -> Unit
    )


    fun get(key: String?): Video?
}