package com.example.placesdemo.responce

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required



  open class PlacesResponce {

    @Required
    @PrimaryKey
    @SerializedName("id")
    @Expose
    open  var id: String? = null

    @SerializedName("next_page_token")
    @Expose
    open  var nextPageToken: String? = null
    @SerializedName("results")
    @Expose
    open  var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    open  var status: String? = null


}
