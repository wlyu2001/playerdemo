package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.network.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel

class RepositoryImp(private val plexService: PlexService, private val realm: Realm) : Repository {

    private fun <T : RealmModel> fetch(
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

        val subscription = observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
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

    override fun getVideo(key: String,
                          success: (Video?) -> Unit,
                          fail: (Error) -> Unit) {

        success(realm.where(Video::class.java).equalTo("key", key).findFirst())

        fetch(
            VideoDetail.url(key),
            VideoDetail::class.java,
            success = { contentDetail ->
                success(contentDetail.video)
            }) { error ->
                fail(error)
        }
    }

    override fun getSection(key: Int, success: (Section?) -> Unit, fail: (Error) -> Unit) {
        success(realm.where(Section::class.java).equalTo("key", key).findFirst())

        fetch(
            Section.url(sectionKey = key),
            Section::class.java,
            success = { section ->
                success(section)
            }) { error ->
            fail(error)
        }
    }

    override fun getSectionList(success: (List<Section>) -> Unit, fail: (Error) -> Unit) {
        realm.where(SectionList::class.java).findFirst()?.let {
            success(it.sections)
        }

        fetch(
            SectionList.url(),
            SectionList::class.java,
            {
                success(it.sections)
            }) { error ->
            fail(error)
        }
    }

    override fun getVideoList(sectionId: Int, success: (List<Video>) -> Unit, fail: (Error) -> Unit) {
        realm.where(VideoList::class.java).equalTo("sectionId", sectionId).findFirst()?.let {
            success(it.videos)
        }

        fetch(
            VideoList.url(sectionId),
            VideoList::class.java,
            {
                success(it.videos)
            }) { error ->
            fail(error)
        }
    }
}