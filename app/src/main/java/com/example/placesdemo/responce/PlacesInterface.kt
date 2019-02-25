package com.example.placesdemo.responce

import io.realm.Realm
import io.realm.RealmResults

interface PlacesInterface {
    fun addOrUpdatePlaces(realm: Realm, places: Result): Boolean
      fun getPlaces(realm: Realm): RealmResults<Result>
}