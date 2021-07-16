package com.asiasquare.byteg.shoppingdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)

    val favoriteItemCount = favoriteItemRepository.favoriteLiveItemCount

    //    private val _itemCount = MutableLiveData<Int>()
//    val itemCount :LiveData<Int>
//        get() = _itemCount

//    init {
//      getCountFavorite()
//    }

//    fun getCountFavorite(): Int? {
//        viewModelScope.launch {
//            _itemCount.value = favoriteItemRepository.getFavoriteItemCount()
//        }
//        return _itemCount.value
//    }

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