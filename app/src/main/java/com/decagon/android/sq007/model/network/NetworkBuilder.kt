package com.decagon.android.sq007.model.network

import com.decagon.android.sq007.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkBuilder {
    fun getPostEndPoint(): PostApiInterface {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PostApiInterface::class.java)
    }
}
