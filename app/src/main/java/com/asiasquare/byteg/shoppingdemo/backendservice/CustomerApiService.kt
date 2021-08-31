package com.asiasquare.byteg.shoppingdemo.backendservice

import android.os.Parcelable
import com.asiasquare.byteg.shoppingdemo.database.customers.NetworkAddress
import com.asiasquare.byteg.shoppingdemo.database.customers.NetworkCustomer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "https://serene-cliffs-31164.herokuapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CustomerApiService{

    @POST("create-customer")
    suspend fun createNewCustomer(@Body customerRequest: CustomerRequest) : NetworkCustomer

}

@Parcelize
data class CustomerRequest(
    val name : String,
    val email : String,
    val address: NetworkAddress,
    val phone: String
) : Parcelable

object CustomerApi{
    val retrofitService : CustomerApiService by lazy {
        retrofit.create(CustomerApiService::class.java)
    }
}