package com.example.placesdemo.api

import android.util.Log
import okhttp3.*


class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val requestBuilder = addHeaders(chain!!.request().newBuilder())
        val response = chain.proceed(requestBuilder!!.build())

        if (!response.isSuccessful && response.code()!= 401 ) {
            // logout logic here
            // send empty response down the chain
            return Response.Builder()
                .code(response.code())
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .body(ResponseBody.create(MediaType.parse("{}"), "{}"))
                .message(response.message())
                .build()
        }

        return response
    }

    private fun addHeaders(requestBuilder: Request.Builder?): Request.Builder? {
        requestBuilder?.header("Content-Type", "application/json")

        return requestBuilder
    }

    private fun shouldLogout(response: Response): Boolean {
        if (response.code() == 401) {
            Log.d("dd","access token expried")
            return true
        }
        if (response.code() != 200) {
            return false
        }
        //ToDo clear access token
        // 401 and auth token means that we need to logout
        /*return (response.code() == 401 &&
                !response.headers().names().contains(AUTH_HEADER_KEY))*/
        return false
    }



}