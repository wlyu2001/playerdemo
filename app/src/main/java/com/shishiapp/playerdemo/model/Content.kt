package com.shishiapp.playerdemo.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
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
@Root(name = "MediaContainer", strict = false)
open class ContentDetail : RealmObject() {
    @field:Element(name = "Video", required = false)
    var content: Content? = null

    companion object {
        fun url(key: String): String {
            return key
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

    @field:Attribute(name = "summary")
    var summary = ""

    @field:Attribute(name = "year", required = false)
    var year = ""

    @field:Attribute(name = "duration", required = false)
    var duration = 0L

    @field:Attribute(name = "thumb", required = false)
    var thumb = ""

    @field:Attribute(name = "art", required = false)
    var art = ""

    @field:Element(name = "Media", required = false)
    var media: Media? = null

    @field:ElementList(entry = "Genre", inline = true, required = false)
    var genres = RealmList<String>()
}




@RealmClass
@Root(name = "Media", strict = false)
open class Media : RealmObject() {
    @field:ElementList(entry = "Part", inline = true)
    var parts = RealmList<Part>()

}

@RealmClass
@Root(name = "Part", strict = false)
open class Part : RealmObject() {
    @PrimaryKey
    @field:Attribute(name = "key")
    var key = ""
}