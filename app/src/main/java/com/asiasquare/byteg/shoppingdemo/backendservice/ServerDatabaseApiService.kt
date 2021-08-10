package com.asiasquare.byteg.shoppingdemo.backendservice

import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @POST("server/ungdungchaua/getsanphamasia0.php")
    suspend fun getDataFirst(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia1.php")
    suspend fun getDataSecond(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia2.php")
    suspend fun getDataThird(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia3.php")
    suspend fun getDataFourth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia4.php")
    suspend fun getDataFifth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia5.php")
    suspend fun getDataSixth(): List<NetworkItem>

    @POST("server/ungdungchaua/getsanphamasia6.php")
    suspend fun getDataSeventh(): List<NetworkItem>

    @POST("server/ungdungchaua/gettatcasanphamasia.php")
    suspend fun getAllData(): List<NetworkItem>
}

object ServerApi{
    val retrofitService : ServerDatabaseApiService by lazy {
        retrofit.create(ServerDatabaseApiService::class.java)
    }
}