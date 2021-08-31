package com.asiasquare.byteg.shoppingdemo.repository

import com.asiasquare.byteg.shoppingdemo.backendservice.CheckoutApiService
import com.asiasquare.byteg.shoppingdemo.backendservice.CheckoutRequest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class DefaultPaymentRepository (
    private val checkoutBackendApi: CheckoutApiService,
    private val workContent: CoroutineContext
):PaymentRepository{


    override suspend fun checkout(
        customer: PaymentRepository.CheckoutCustomer,
        currency: PaymentRepository.CheckoutCurrency,
        mode: PaymentRepository.CheckoutMode,
        amount: Int,
        metadata: String
    )= withContext(workContent) {
        flowOf(
            kotlin.runCatching {
                checkoutBackendApi.checkout(
                    CheckoutRequest(
                        customer.value,
                        currency.value,
                        mode.value,
                        amount,
                        metadata
                    )
                )
            }
        )
    }
}