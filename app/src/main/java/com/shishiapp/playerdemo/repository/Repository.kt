package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.network.model.Video

interface Repository {

    fun getVideo(key: String, success: (Video?) -> Unit, fail: (Error) -> Unit)


    fun getSection(key: Int, success: (Section?) -> Unit, fail: (Error) -> Unit)

    fun getSectionList(success: (List<Section>) -> Unit, fail: (Error) -> Unit)

    fun getVideoList(sectionId: Int, success: (List<Video>) -> Unit, fail: (Error) -> Unit)
}