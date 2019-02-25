package com.example.placesdemo

import android.app.Application
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .name("places.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}

