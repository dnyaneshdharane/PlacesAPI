package com.example.placesdemo.adapter

import android.app.IntentService
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.example.placesdemo.R


object GlideManager {

    fun loadImageCircularCrop(url: String, view: View) {
        if (view != null)
            Glide.with(view.context).load(url).apply(RequestOptions().circleCrop().placeholder(R.drawable.ic_action_temp)).into(view as ImageView)
    }

    fun loadImage(url: String, view: View) {
        Glide.with(view.context).load(url)
                .transition(withCrossFade())
                .apply(RequestOptions().fitCenter().transform( RoundedCorners(10)).placeholder(R.drawable.ic_action_temp)).into(view as ImageView)

    }




    class LoadImagesIntentService : IntentService("LoadImagesIntentService") {

        override fun onHandleIntent(intent: Intent?) {
            val listOfUrls = intent?.getStringArrayListExtra("list")
            for (i in 0 until listOfUrls!!.size) {
                val future = Glide.with(applicationContext)
                        .load(listOfUrls[i] as String)
                        .downloadOnly(900, 700)
                val cacheFile = future.get()
            }
        }
    }

}