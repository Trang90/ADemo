package com.asiasquare.byteg.shoppingdemo

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.repository.CartRepository
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)
    private val cartRepository = CartRepository(database)

    val favoriteItemCount = favoriteItemRepository.favoriteLiveItemCount
    val cartAmountItemCount =  cartRepository.cartLiveAmountItemCount

    /**
     * Factory for constructing CatalogFragmentViewModel with parameter
     */
    class Factory(
        private val app: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}