package com.asiasquare.byteg.shoppingdemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(private val database: AsiaDatabase){

    val favoriteItems: LiveData<List<FavoriteItem>> = database.favoriteItemDao.getAllItemsInFavoriteList()

    val favoriteLiveItemCount: LiveData<Int> = database.favoriteItemDao.getLiveItemsCount()

    suspend fun addFavoriteItem(item: FavoriteItem){
        withContext(Dispatchers.IO){
            database.favoriteItemDao.insert(item)
            Log.d("REPO FAV", "Successful add item to favorite")
        }
    }

    suspend fun deleteFavoriteItem(item: FavoriteItem){
        withContext(Dispatchers.IO){
            database.favoriteItemDao.delete(item)
            Log.d("REPO FAV", "Successful delete item in favorite")
        }
    }

    suspend fun getFavoriteItemById(id: Int) : FavoriteItem? {
        var item : FavoriteItem? = null
        withContext(Dispatchers.IO){
            item = database.favoriteItemDao.get(id)
        }
        return item
    }


}