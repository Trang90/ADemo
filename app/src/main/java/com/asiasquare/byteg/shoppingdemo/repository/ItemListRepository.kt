package com.asiasquare.byteg.shoppingdemo.repository

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.asiasquare.byteg.shoppingdemo.backendservice.ServerDatabaseApiService
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.util.networkBoundResource
import kotlinx.coroutines.delay

//
//class ItemListRepository( private val api: ServerDatabaseApiService, private val database: AsiaDatabase) {
//
//    //val localItems: LiveData<List<LocalItem>> = database.itemDao.getAllItems()
//
//    private val itemDao = database.itemDao
//
//    fun getlocalItems() = networkBoundResource(
//        query = {
//            database.itemDao.getAllItems()
//        },
//        fetch = {
//            delay(2000)
//            api.getDataFirst()
//        },
//        saveFetchResult = {
//        database.withTransaction {
//                itemDao.clearAllItems()
//                itemDao.insertItems(it)
//            }}
//
//    )



//}
