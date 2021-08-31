package com.asiasquare.byteg.shoppingdemo.payment.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.repository.CartRepository
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsiaDatabase.getInstance(application)
    private val cartRepository = CartRepository(database)

    val itemInCartList : LiveData<MutableList<ShoppingBasketItem>> = cartRepository.cartItems


    private val _navigateToDetail = MutableLiveData<LocalItem?>()
    val navigateToDetail : LiveData<LocalItem?>
        get() = _navigateToDetail

    fun onDelete(item: ShoppingBasketItem) {
        viewModelScope.launch {
            if (cartRepository.getCartItemById(item.itemId) != null) {
                cartRepository.deleteCartItem(item)
                Log.d("Favorite viewmodel", "Item da duoc xoa")
            }
        }
    }

    fun onDetailClick( item: ShoppingBasketItem){
        _navigateToDetail.value = item.asDomainItem().asLocalItem()
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }


    class Factory(
        private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(OrderViewModel::class.java)){
                return OrderViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}