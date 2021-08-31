package com.asiasquare.byteg.shoppingdemo.payment.stripepayment

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.Parcelize
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.repository.CartRepository
import com.asiasquare.byteg.shoppingdemo.repository.PaymentRepository
import com.asiasquare.byteg.shoppingdemo.util.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

internal class UIViewModel(
    application: Application,
    private val repository: PaymentRepository
) : AndroidViewModel(application){

    private val database = AsiaDatabase.getInstance(application)
    private val cartRepository = CartRepository(database)

    val itemsInList = MutableLiveData<Int>()

    private val _subTotal = MutableLiveData<Double>()
    val subTotal : LiveData<Double>
        get() = _subTotal

    private val _total = MutableLiveData<Double>()
    val total : LiveData<Double>
        get() = _total

    private val _shippingPrice = MutableLiveData<Double>()
    val shippingPrice : LiveData<Double>
        get() = _shippingPrice

    private val _stripeAmount = MutableLiveData<Int>()
    val stripeAmount : LiveData<Int>
        get() = _stripeAmount

    /** Check payment status **/
    val inProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()

    val continueShopping = MutableLiveData<Boolean>()


    private val _itemList = MutableLiveData<List<Item>>()
    val itemList : LiveData<List<Item>>
        get() = _itemList


    val itemInCartList : LiveData<MutableList<ShoppingBasketItem>> = cartRepository.cartItems


    init {
        continueShopping.value = false
    }

    fun prepareCheckout(customer: PaymentRepository.CheckoutCustomer, mode: PaymentRepository.CheckoutMode, amount: Int, metadata: String) =
        liveData {
            continueShopping.value = false
            inProgress.postValue(true)
            status.postValue("Chuan bi qua trinh thanh toan...")

            val checkoutResponse = repository.checkout(
                customer, PaymentRepository.CheckoutCurrency.EUR, mode, amount, metadata
            ).single()

            checkoutResponse.fold(
                onSuccess = {response ->
                    status.postValue("San sang tien hanh thanh toan"
                    )
                },
                onFailure = {
                    status.postValue("Da co loi xuat hien trong qua tinh chuan bi thanh toan!"
                    )
                }
            )
            inProgress.postValue(false)
            emit(checkoutResponse.getOrNull())
        }

    fun getItemsInCartAsString(): String{
        return cartRepository.getAllItemsAsString()
    }


    fun calcutalePrice(list: MutableList<ShoppingBasketItem>){
        var price : Double = 0.00
        for(item in list){
            price += item.itemPrice
        }
        _subTotal.value = price.round(2)
        if(haveShippingPrice(price)){
            _total.value = (price + 5.99).round(2)
            _shippingPrice.value = 5.99
            _stripeAmount.value = getStripePrice(price + 5.99)
        }else{
            _total.value = price.round(2)
            _shippingPrice.value = 0.00
            _stripeAmount.value = getStripePrice(price)
        }

    }

    private fun haveShippingPrice(price: Double): Boolean {
        return price <= 50
    }

    private fun getStripePrice(price: Double): Int {
        val amount = price * 100
        return amount.roundToInt()
    }

    fun countItem(list: MutableList<ShoppingBasketItem>){
        itemsInList.value = list.size
    }

    fun onClear(){
        viewModelScope.launch {
            clear()
        }
    }
    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.basketItemDao.clearShoppingBasket()
        }
    }
}


@Parcelize
data class Item(
    val imageSrc : String,
    val name: String,
    val stock: String,
    val price: String
): Parcelable