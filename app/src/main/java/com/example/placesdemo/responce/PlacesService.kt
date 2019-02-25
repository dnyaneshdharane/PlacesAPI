package com.realmkotlinexample.dbservice


import com.example.placesdemo.responce.PlacesInterface
import com.example.placesdemo.responce.PlacesResponce
import com.example.placesdemo.responce.Result
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults


class PlacesService : PlacesInterface {
    override fun addOrUpdatePlaces(realm: Realm, places: Result): Boolean {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(places)
            realm.commitTransaction()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun getPlaces(realm: Realm): RealmResults<Result> {
        return realm.where(Result::class.java).findAll()
    }


}