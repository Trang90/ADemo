package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem

@Dao
interface ItemDao {

    @Insert
    fun insert(item : LocalItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item : LocalItem)

    @Delete
    fun deleteItem(item : LocalItem)

    @Query(value = "DELETE FROM local_items_table")
    fun clearAllItems()

    @Query(value = "SELECT * FROM local_items_table")
    fun getAllItems() : LiveData<List<LocalItem>>

}