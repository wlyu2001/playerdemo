package com.shishiapp.playerdemo

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)

    }

}
