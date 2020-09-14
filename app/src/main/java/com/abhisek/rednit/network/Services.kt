package com.abhisek.rednit.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface RednitServices {
    @GET("json/get/ceiNKFwyaa?indent=2")
    suspend fun getProfiles(): List<NetworkProfile>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build();

object Network{
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.json-generator.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service : RednitServices by lazy {  retrofit.create(RednitServices::class.java) }
}