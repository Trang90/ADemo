package com.asiasquare.byteg.shoppingdemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository (private val database: AsiaDatabase){

    val orders : LiveData<List<LocalOrder>> = database.localOrderDao.getAllLocalOrder()

    suspend fun addLocalOrder(localOrder: LocalOrder){
        withContext(Dispatchers.IO){
            database.localOrderDao.insert(localOrder)
            Log.d("REPO", "Successful add order")
        }
    }

    suspend fun deleteLocalOrder(localOrder: LocalOrder){
        withContext(Dispatchers.IO){
            database.localOrderDao.deleteOrder(localOrder)
            Log.d("REPO", "Successful delete order")
        }
    }

}