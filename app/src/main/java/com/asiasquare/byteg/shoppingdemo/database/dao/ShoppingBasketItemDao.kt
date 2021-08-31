package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem

@Dao
interface ShoppingBasketItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item : ShoppingBasketItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item : ShoppingBasketItem)

    @Delete
    fun deleteItem(item : ShoppingBasketItem)

    @Query(value = "DELETE FROM shopping_basket_table")
    fun clearShoppingBasket()

    @Query(value = "SELECT * FROM shopping_basket_table")
    fun getAllItemsInBasket() : LiveData<MutableList<ShoppingBasketItem>>

    @Query(value = "SELECT * from shopping_basket_table WHERE itemId = :id")
    fun get(id : Int) : ShoppingBasketItem?

    @Query("SELECT * FROM shopping_basket_table ORDER BY itemId DESC LIMIT 1")
    fun getItem(): ShoppingBasketItem?

    @Query(value = "SELECT COUNT(itemId) from shopping_basket_table")
    fun getCartItemsCount() : Int

    @Query(value = "SELECT SUM(item_amount) from shopping_basket_table")
    fun getLiveAmountItemsCount() : LiveData<Int>

}