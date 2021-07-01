package com.asiasquare.byteg.shoppingdemo.backendservice

import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerDatabaseApiService{

    @POST("get_data")
    suspend fun getData(): List<NetworkItem>

}