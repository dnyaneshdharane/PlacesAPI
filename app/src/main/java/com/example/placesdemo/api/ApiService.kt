package com.example.placesdemo.api

import com.example.placesdemo.responce.PlacesResponce
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {


    @GET("place/nearbysearch/json?")
    fun doPlaces(
        @Query(value = "type", encoded = true) type: String, @Query(
            value = "location",
            encoded = true
        ) location: String, @Query(value = "name", encoded = true) name: String, @Query(
            value = "opennow",
            encoded = true
        ) opennow: Boolean, @Query(value = "rankby", encoded = true) rankby: String, @Query(
            value = "key",
            encoded = true
        ) key: String
    ): retrofit2.Call<PlacesResponce>


    @GET("distancematrix/json") // origins/destinations:  LatLng as string
    fun getDistance(@Query("key") key: String, @Query("origins") origins: String, @Query("destinations") destinations: String): retrofit2.Call<ResponseBody>


    companion object Factory {
        val GOOGLE_PLACE_API_KEY = "AIzaSyBuRyukdv3-7ZHuToIPI67ZWj_MZrwX-nE"

        var base_url = "https://maps.googleapis.com/maps/api/"


        fun create(): ApiService {
            val okhttpClientBuilder = OkHttpClient.Builder()
            okhttpClientBuilder.networkInterceptors().add(NetworkInterceptor())
            okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
            okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
            okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
            okhttpClientBuilder.retryOnConnectionFailure(true)
            //Retrofit log request response line
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okhttpClientBuilder.addInterceptor(logging)


            //interceptor to interest request

            //json convertFactory
            val builder = GsonBuilder()
            builder.serializeNulls()
            builder.excludeFieldsWithoutExposeAnnotation()
            val gson = builder.setLenient().create()
//            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .client(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiService::class.java)


        }
    }


}