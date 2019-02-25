//package com.example.placesdemo.responce
//
//import android.app.Activity;
//import android.app.Application;
//import android.support.v4.app.Fragment;
//import io.realm.Realm;
//import io.realm.RealmResults;
//import java.awt.print.Book
//
//
//
//
//class RealmController {
//
//    private lateinit var realm: Realm
//
//    constructor(application: Application) {
//        realm = Realm.getDefaultInstance()
//    }
//
//    companion object {
//        var instance: RealmController? = null
//        fun with(fragment: Fragment): RealmController {
//
//            if (instance == null) {
//                instance = RealmController(fragment.activity!!.application)
//            }
//            return instance as RealmController
//        }
//
//        fun with(activity: Activity): RealmController {
//
//            if (instance == null) {
//                instance = RealmController(activity.application)
//            }
//            return instance as RealmController
//        }
//
//        fun with(application: Application): RealmController {
//
//            if (instance == null) {
//                instance = RealmController(application)
//            }
//            return instance as RealmController
//        }
//
//
//    }
//
//    fun getRealm(): Realm {
//
//        return realm
//    }
//
//    //Refresh the realm istance
//    fun refresh() {
//
//        realm.refresh()
//    }
//
//    //clear all objects from Book.class
//    fun clearAll() {
//
//        realm.beginTransaction()
//        realm.clear(PlacesResponce.Result::class.java)
//        realm.commitTransaction()
//    }
//
//}