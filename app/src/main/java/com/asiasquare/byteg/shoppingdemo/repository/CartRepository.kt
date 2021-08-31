package com.asiasquare.byteg.shoppingdemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepository(private val database: AsiaDatabase){

    val cartItems: LiveData<MutableList<ShoppingBasketItem>> = database.basketItemDao.getAllItemsInBasket()

    val cartLiveAmountItemCount: LiveData<Int> = database.basketItemDao.getLiveAmountItemsCount()

    suspend fun addCartItem(item: ShoppingBasketItem){
        withContext(Dispatchers.IO){
            database.basketItemDao.insert(item)
            Log.d("REPO FAV", "Successful add item to Shopping cart")
        }
    }

    suspend fun updateCartItem(item: ShoppingBasketItem){
        withContext(Dispatchers.IO){
            database.basketItemDao.update(item)
            Log.d("REPO FAV", "Successful update item to Shopping cart")
        }
    }


    suspend fun deleteCartItem(item: ShoppingBasketItem){
        withContext(Dispatchers.IO){
            database.basketItemDao.deleteItem(item)
            Log.d("REPO FAV", "Successful delete item in Shopping cart")
        }
    }

    suspend fun getCartItemById(id: Int) : ShoppingBasketItem? {
        var item : ShoppingBasketItem? = null
        withContext(Dispatchers.IO){
            item = database.basketItemDao.get(id)
        }
        return item
    }

    fun getAllItemsAsString() : String{
        var result : String = ""
        if(!cartItems.value.isNullOrEmpty()){
            val itemList = cartItems.value!!.toList()
            for(item in itemList){
                result += "( ${item.itemName} : ${item.itemAmount} )"
            }
        }
        return result
    }


}