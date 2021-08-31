package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalOrder

@Dao
interface LocalOrderDao {

    @Insert
    fun insert(order: LocalOrder)

    @Update
    fun update(order: LocalOrder)

    @Query(value = "SELECT * FROM local_order_table WHERE orderId =:id")
    fun get(id : String) : LocalOrder

    @Delete
    fun deleteAllSelectOrder(orders : List<LocalOrder>) : Int

    @Delete
    fun deleteOrder(order: LocalOrder) : Int

    @Query(value = "DELETE FROM local_order_table")
    fun clear()

    @Query(value = "SELECT * FROM local_order_table")
    fun getAllLocalOrder() : LiveData<List<LocalOrder>>

    @Query(value = "SELECT COUNT(orderId) FROM local_order_table")
    fun getCount() : Int
}