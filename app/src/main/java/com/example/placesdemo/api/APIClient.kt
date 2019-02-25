package com.example.placesdemo.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {

    companion object   {
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