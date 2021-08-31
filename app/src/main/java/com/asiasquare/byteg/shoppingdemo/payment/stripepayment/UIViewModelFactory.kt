package com.asiasquare.byteg.shoppingdemo.payment.stripepayment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asiasquare.byteg.shoppingdemo.backendservice.BackendApiFactory
import com.asiasquare.byteg.shoppingdemo.repository.DefaultPaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
* Factory for constructing UIViewModel with parameter
*/
class UIViewModelFactory(
    private val application: Application,
    private val workContext: CoroutineContext = Dispatchers.IO
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val checkoutBackendApi = BackendApiFactory(application).createCheckout()

        val repository = DefaultPaymentRepository(
            checkoutBackendApi,
            workContext
        )

        return UIViewModel(
            application,
            repository
        ) as T
    }
}