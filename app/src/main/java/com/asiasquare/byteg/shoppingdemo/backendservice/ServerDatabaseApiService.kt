package com.asiasquare.byteg.shoppingdemo.backendservice

import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "http://www.germanyshoppingsquare.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ServerDatabaseApiService{

    @POST("server/ungdungchaua/getsanphamasiaandroid0.php?page=1")
    suspend fun getDataFirst(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid1.php?page=1")
    suspend fun getDataSecond(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid2.php?page=1")
    suspend fun getDataThird(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid3.php?page=1")
    suspend fun getDataFourth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid4.php?page=1")
    suspend fun getDataFifth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid5.php?page=1")
    suspend fun getDataSixth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasiaandroid6.php?page=1")
    suspend fun getDataSeventh(): List<NetworkItem>
}

object ServerApi{
    val retrofitService : ServerDatabaseApiService by lazy {
        retrofit.create(ServerDatabaseApiService::class.java)
    }
}