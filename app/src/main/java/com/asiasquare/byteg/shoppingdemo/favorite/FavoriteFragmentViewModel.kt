package com.asiasquare.byteg.shoppingdemo.favorite

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.asiasquare.byteg.shoppingdemo.repository.CartRepository
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class
FavoriteFragmentViewModel(application: Application) : AndroidViewModel(application) {


    private val _navigateToDetail = MutableLiveData<LocalItem?>()
    val navigateToDetail : MutableLiveData<LocalItem?>
        get() = _navigateToDetail

    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)
    private val cartItemRepository = CartRepository(database)

    val favoriteList = favoriteItemRepository.favoriteItems
    private var itemAmount: Int = 1

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    fun onDeleteFavoriteClicking(favorite: FavoriteItem) {
        viewModelScope.launch {
            if (favoriteItemRepository.getFavoriteItemById(favorite.itemId) != null) {
                favoriteItemRepository.deleteFavoriteItem(favorite)
                Log.d("Favorite viewmodel", "Item da duoc xoa")
            }
        }
    }


    fun onTaskSwiped(favorite: FavoriteItem) = viewModelScope.launch {
        favoriteItemRepository.deleteFavoriteItem(favorite)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(favorite))
    }

    fun onUndoDeleteClick(favorite: FavoriteItem) = viewModelScope.launch {
        favoriteItemRepository.addFavoriteItem(favorite)
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: FavoriteItem) : TasksEvent()
    }

    fun onDetailClick(item: FavoriteItem){
        _navigateToDetail.value = item.asDomainItem().asLocalItem()
    }



    fun onCartClicking(favorite: FavoriteItem) {
        viewModelScope.launch {
            //Try to get this item from current cart
            val item = cartItemRepository.getCartItemById(favorite.asDomainItem().asCartItem(itemAmount).itemId)

            //Case: already have this item in cart
            if (item != null) {
                //update the item amount
                itemAmount = item.itemAmount
                cartItemRepository.updateCartItem(favorite.asDomainItem().asCartItem(itemAmount))
                Log.d("Cart viewmodel", "So Luong da duoc update")

            } else
            //Add this new item to the cart
                cartItemRepository.addCartItem(favorite.asDomainItem().asCartItem(itemAmount))
            Log.d("Cart viewmodel","Them $itemAmount Item vao Shopping Basket")
        }
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }

    /**
     * Factory for constructing CatalogFragmentViewModel with parameter
     */
    class Factory(
        private val app: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteFragmentViewModel::class.java)) {
                return FavoriteFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}