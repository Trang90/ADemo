package com.asiasquare.byteg.shoppingdemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.customers.Customer
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer
import com.asiasquare.byteg.shoppingdemo.database.customers.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomersRepository (private val database: AsiaDatabase){

    val customers : LiveData<List<Customer>> = Transformations.map(
        database.localCustomerDatabaseDao.getAllLocalCustomers()
    ){
        it.asDomainModel()
    }

    suspend fun addLocalCustomer(localCustomer: LocalCustomer){
        withContext(Dispatchers.IO){
            database.localCustomerDatabaseDao.insert(localCustomer)
            Log.d("REPO", "Successful add customer")
        }
    }

    suspend fun deleteLocalCustomer(localCustomer: LocalCustomer){
        withContext(Dispatchers.IO){
            database.localCustomerDatabaseDao.deleteCustomer(localCustomer)
            Log.d("REPO", "Successful delete customer")
        }
    }

}