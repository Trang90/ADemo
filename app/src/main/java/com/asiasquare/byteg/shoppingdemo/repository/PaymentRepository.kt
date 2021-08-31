package com.asiasquare.byteg.shoppingdemo.repository

import com.asiasquare.byteg.shoppingdemo.backendservice.CheckoutResponse
import kotlinx.coroutines.flow.Flow

internal interface PaymentRepository{
    sealed class  CheckoutCustomer(val value: String){
        object New : CheckoutCustomer(value = "new")
        object Returning : CheckoutCustomer(value = "returning")
        data class WithId(val customerId: String) : CheckoutCustomer(customerId)
    }

    enum class CheckoutCurrency(val value: String) {
        USD("usd"),
        EUR("eur")
    }

    enum class CheckoutMode(val value: String) {
        Setup("setup"),
        Payment("payment")
    }

    suspend fun checkout(
        customer: CheckoutCustomer,
        currency: CheckoutCurrency,
        mode: CheckoutMode,
        amount: Int,
        metadata: String
    ): Flow<Result<CheckoutResponse>>
}