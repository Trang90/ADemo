package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem

@Dao
interface ShoppingBasketItemDao {

    @Insert
    fun insert(item : ShoppingBasketItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item : ShoppingBasketItem)

    @Delete
    fun deleteItem(item : ShoppingBasketItem)

    @Query(value = "DELETE FROM shopping_basket_table")
    fun clearShoppingBasket()

    @Query(value = "SELECT * FROM shopping_basket_table")
    fun getAllItemsInBasket() : LiveData<List<ShoppingBasketItem>>

}