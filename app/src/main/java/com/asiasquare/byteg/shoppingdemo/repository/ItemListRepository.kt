package com.asiasquare.byteg.shoppingdemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.asiasquare.byteg.shoppingdemo.backendservice.ServerApi
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ItemRepository(private val database: AsiaDatabase) {

    val localItems: LiveData<List<LocalItem>> = database.itemDao.getAllItems()


    suspend fun getDataByCatalogId(catalogId: Int): List<NetworkItem>{
        var listResult = listOf<NetworkItem>()
        try {
            withContext(Dispatchers.IO){
                listResult = when(catalogId){
                    0-> ServerApi.retrofitService.getDataFirst()
                    1-> ServerApi.retrofitService.getDataSecond()
                    2-> ServerApi.retrofitService.getDataThird()
                    3-> ServerApi.retrofitService.getDataFourth()
                    4-> ServerApi.retrofitService.getDataFifth()
                    5-> ServerApi.retrofitService.getDataSixth()
                    else -> ServerApi.retrofitService.getDataSeventh()
                }
            }

        }catch(e: Exception) {
            e.message?.let { Log.d("Get data $catalogId",it) }
        }
        return listResult
    }


    suspend fun addLocalItem(item: LocalItem){
        withContext(Dispatchers.IO){
            database.itemDao.insert(item)
            Log.d("REPO FAV", "Successful add item to local database")
        }
    }

    /**
     * Add all network items to local database
     */
    suspend fun addListLocalItem(items: List<NetworkItem>){
        withContext(Dispatchers.IO){
            val resultContainer = NetworkItemContainer(items)
            database.itemDao.insertList(resultContainer.asLocalItems())
            Log.d("REPO FAV", "Successful add all items to local database")
        }
    }

    suspend fun deleteLocalItem(item: LocalItem){
        withContext(Dispatchers.IO){
            database.itemDao.deleteItem(item)
            Log.d("REPO FAV", "Successful delete item in local database")
        }
    }


}
