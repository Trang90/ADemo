package com.asiasquare.byteg.shoppingdemo.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.repository.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CartFragmentViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * List of catalog, observe this to get tha change in database
     */

    private val database = AsiaDatabase.getInstance(application)
    private val cartRepository = CartRepository(database)

    val cartList = cartRepository.cartItems

    private val _navigateToDetail = MutableLiveData<LocalItem?>()
    val navigateToDetail : LiveData<LocalItem?>
        get() = _navigateToDetail

    private var itemAmount: Int = 1


    private val _totalPrice = MutableLiveData<String?>()
    val totalPrice :  MutableLiveData<String?>
        get() = _totalPrice


    private val cartEventChannel = Channel<TasksEvent>()
    val tasksEvent = cartEventChannel.receiveAsFlow()


    fun onDeleteCartClicking(cart: ShoppingBasketItem) {
        viewModelScope.launch {
            if (cartRepository.getCartItemById(cart.itemId) != null) {
                cartRepository.deleteCartItem(cart)
                Log.d("Favorite viewmodel", "Item da duoc xoa")
            }
        }
    }

    fun onAddBtnClicking(cart: ShoppingBasketItem) {
        viewModelScope.launch {
            //Try to get this item from current cart
            val item = cartRepository.getCartItemById(cart.itemId)

            //Case: already have this item in cart
            if (item != null && item.itemAmount < 50) {
                //update the item amount
                itemAmount = item.itemAmount +1
                cartRepository.updateCartItem(cart.asDomainItem().asCartItem(itemAmount))
                Log.d("Cart viewmodel", "So Luong da duoc update")

            } else
            //Add this new item to the cart
                cartRepository.addCartItem(cart.asDomainItem().asCartItem(itemAmount))
            Log.d("Cart viewmodel","Them $itemAmount Item vao Shopping Basket")
        }
    }


    fun onMinusBtnClicking(cart: ShoppingBasketItem) {
        viewModelScope.launch {
            val item = cartRepository.getCartItemById(cart.itemId)

            //Case: already have this item in card
            if (item != null && item.itemAmount > 1) {
                //update the item amount
                itemAmount = item.itemAmount - 1
                cartRepository.updateCartItem(cart.asDomainItem().asCartItem(itemAmount))
                Log.d("Cart viewmodel", "So Luong da duoc update")

            } else
            //Add this new item to the cart
                cartRepository.addCartItem(cart.asDomainItem().asCartItem(itemAmount))
            Log.d("Cart viewmodel","Xoa $itemAmount Item trong Shopping Basket")
        }
    }

    fun onTaskSwiped(cart: ShoppingBasketItem) = viewModelScope.launch {
        cartRepository.deleteCartItem(cart)
        cartEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(cart))
    }

    fun onUndoDeleteClick(cart: ShoppingBasketItem) = viewModelScope.launch {
        cartRepository.addCartItem(cart)
    }

    fun onDetailClick( item: ShoppingBasketItem){
        _navigateToDetail.value = item.asDomainItem().asLocalItem()
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: ShoppingBasketItem) : TasksEvent()
    }


    /**
     * Factory for constructing CartViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CartFragmentViewModel::class.java)){
                return CartFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}