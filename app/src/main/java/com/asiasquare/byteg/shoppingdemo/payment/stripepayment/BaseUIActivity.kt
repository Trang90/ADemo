package com.asiasquare.byteg.shoppingdemo.payment.stripepayment

import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.repository.PaymentRepository
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult


internal abstract class BaseUIActivity : AppCompatActivity(){
    protected val viewModel: UIViewModel by viewModels{
        UIViewModelFactory(
            application
        )
    }
    protected var amount : Int = -1

    protected var itemsInCart : String = "Danh sach gio hang"

    protected var temporaryCustomerId: String? = null

    protected val merchantName: String = "Asia Shopping Square"

    protected val isCustomerEnabled: Boolean = true

    protected val isReturningCustomer: Boolean = false

    protected val isSetupIntent: Boolean = false

    protected val googlePayConfig: PaymentSheet.GooglePayConfiguration? = null



    protected val customer: PaymentRepository.CheckoutCustomer
        get() = if (isCustomerEnabled && isReturningCustomer) {
            PaymentRepository.CheckoutCustomer.Returning
        } else if (isCustomerEnabled) {
            temporaryCustomerId?.let {
                PaymentRepository.CheckoutCustomer.WithId(it)
            } ?: PaymentRepository.CheckoutCustomer.New
        } else {
            PaymentRepository.CheckoutCustomer.New
        }

    protected val mode: PaymentRepository.CheckoutMode
        get() = if (isSetupIntent) {
            PaymentRepository.CheckoutMode.Setup
        } else {
            PaymentRepository.CheckoutMode.Payment
        }

    protected open fun prepareCheckout(
        onSuccess : (PaymentSheet.CustomerConfiguration, String) -> Unit
    ){
        itemsInCart = viewModel.getItemsInCartAsString()
        viewModel.prepareCheckout(customer,mode,amount,itemsInCart)
            .observe(this) { checkoutResponse ->
                if (checkoutResponse != null) {
                    temporaryCustomerId = checkoutResponse.customerId

                    //Init PaymentConfiguration with the publishable key returned from the backend
                    PaymentConfiguration.init(this, checkoutResponse.publishableKey)

                    onSuccess(
                        PaymentSheet.CustomerConfiguration(
                            id = checkoutResponse.customerId,
                            ephemeralKeySecret = checkoutResponse.customerEphemeralKeySecret
                        ),
                        checkoutResponse.intentClientSecret
                    )
                }
            }
    }

    protected open fun onPaymentSheetResult(
        paymentResult: PaymentSheetResult
    ){
        when (paymentResult) {
            is PaymentSheetResult.Canceled -> {
                viewModel.status.value = "Da huy qua trinh thanh toan."
                viewModel.continueShopping.value = false
            }
            is PaymentSheetResult.Failed -> {
                viewModel.continueShopping.value = false
                viewModel.status.value = "Khong the thuc hien thanh toan."
                Log.e("App", "Got error: ${paymentResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                viewModel.status.value = getString(R.string.thanh_toan_thanh_cong)
                viewModel.continueShopping.value = true
            }
        }
//        viewModel.status.value = paymentResult.toString()

    }
}