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
open class SectionList : RealmObject() {
    @field:ElementList(entry = "Directory", inline = true)
    var sections = RealmList<Section>()

    companion object {
        fun url(): String {
            return "library/sections"
        }
    }

}

@RealmClass
@Root(name = "Directory", strict = false)
open class Section : RealmObject() {


    @PrimaryKey
    @field:Attribute(name = "key")
    var key = 0

    @field:Attribute(name = "title")
    var title = ""

    @field:Attribute(name = "type")
    var type = ""

    companion object {

        fun url(sectionKey: Int): String {
            return "library/section/$sectionKey/"
        }

    }

}