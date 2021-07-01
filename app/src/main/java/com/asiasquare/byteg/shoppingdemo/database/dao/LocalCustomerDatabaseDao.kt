package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer

@Dao
interface LocalCustomerDatabaseDao {

    @Insert
    fun insert(customer: LocalCustomer)

    @Update
    fun update(customer: LocalCustomer)

    @Query(value = "SELECT * FROM local_customer_table WHERE customerId =:id")
    fun get(id : Long) : LocalCustomer

    @Delete
    fun deleteAllSelectCustomers(customers : List<LocalCustomer>) : Int

    @Delete
    fun deleteCustomer(customer: LocalCustomer) : Int

    @Query(value = "DELETE FROM local_customer_table")
    fun clear()

    @Query(value = "SELECT * FROM local_customer_table ORDER BY customer_name DESC")
    fun getAllLocalCustomers() : LiveData<List<LocalCustomer>>

    @Query(value = "SELECT COUNT(customerId) FROM local_customer_table")
    fun getCount() : Int
}