package com.asiasquare.byteg.shoppingdemo.backendservice

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckoutApiService{

    @POST("checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest) : CheckoutResponse

}

@Parcelize
data class CheckoutResponse(
    @Json(name = "publishable_key")
    val publishableKey: String,
    @Json(name = "customer_id")
    val customerId : String,
    @Json(name = "ephemeral_key")
    val customerEphemeralKeySecret: String,
    @Json(name = "intent_client_secret")
    val intentClientSecret : String
) : Parcelable {

}

@Parcelize
data class CheckoutRequest(
    @Json(name = "customer_id")
    val customerId : String,
    val currency: String,
    val mode: String,
    @Json(name = "amount")
    val paymentAmount: Int,
    val metadata: String
) : Parcelable