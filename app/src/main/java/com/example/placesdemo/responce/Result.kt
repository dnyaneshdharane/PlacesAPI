package com.example.placesdemo.responce

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Result : RealmObject(){

    @Required
    @PrimaryKey
    @SerializedName("id")
    @Expose
    open  var id: String? = null
    @SerializedName("name")
    @Expose
    open  var name: String? = null

    @SerializedName("place_id")
    @Expose
    open  var placeId: String? = null
    @SerializedName("rating")
    @Expose
    open  var rating: Double? = null
    @SerializedName("reference")
    @Expose
    open  var reference: String? = null
    @SerializedName("scope")
    @Expose
    open  var scope: String? = null
    @SerializedName("vicinity")
    @Expose
    open  var vicinity: String? = null
    @SerializedName("icon")
    @Expose
    open  var icon: String? = null

}
