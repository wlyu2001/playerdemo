package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.model.SectionList
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoDetail
import com.shishiapp.playerdemo.model.VideoList
import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import okhttp3.HttpUrl

class RepositoryImp(private val plexService: PlexService, private val realm: Realm) : Repository {

    private val baseUrl = HttpUrl.parse(Constants.baseUrl)!!

    override fun <T : RealmModel> get(
        path: String,
        type: Class<T>,
        success: (T) -> Unit,
        fail: (Error) -> Unit
    ) {
        val observable = when (type) {
            SectionList::class.java ->
                plexService.getSectionList(path)
            VideoList::class.java ->
                plexService.getContentList(path)
            VideoDetail::class.java ->
                plexService.getContentDetail(path)

            else -> null
        }

        observable?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ response ->
                realm.beginTransaction()
                realm.insertOrUpdate(response)
                realm.commitTransaction()
                success(response as T)
            },
                { error ->
                    fail(Error(error))
                })
    }

    override fun get(key: String?): Video? {
        return realm.where(Video::class.java).equalTo("key", key).findFirst()
    }
}