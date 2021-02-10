package com.shishiapp.playerdemo.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@RealmClass
@Root(name = "MediaContainer", strict = false)
open class ContentList : RealmObject() {
    @field:ElementList(entry = "Video", inline = true)
    var contents = RealmList<Content>()

    companion object {
        fun url(sectionId: Int): String {
            return "library/sections/$sectionId/all"
        }
    }

}


@RealmClass
@Root(name = "Video", strict = false)
open class Content : RealmObject() {
    @PrimaryKey
    @field:Attribute(name = "key")
    var key = ""

    @field:Attribute(name = "title")
    var title = ""

    @field:Attribute(name = "year", required = false)
    var year = ""

    @field:Attribute(name = "thumb")
    var thumb = ""

//    @field:Element(name = "Media")
//    var media = null

    @field:ElementList(entry = "Genre", inline = true, required = false)
    var genres = RealmList<String>()

}