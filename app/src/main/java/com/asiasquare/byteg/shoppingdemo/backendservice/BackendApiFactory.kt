package com.asiasquare.byteg.shoppingdemo.backendservice
import android.content.Context
import com.asiasquare.byteg.shoppingdemo.Settings
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal class BackendApiFactory internal constructor(private val backendUrl: String){
    constructor(context: Context) : this(Settings(context).backendBaseUrl)

    fun createCheckout(): CheckoutApiService{
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(backendUrl)
            .client(httpClient)
            .build()
            .create(CheckoutApiService::class.java)
    }

    private companion object {
        private const val TIMEOUT_SECONDS = 15L
    }
}