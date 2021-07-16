package com.asiasquare.byteg.shoppingdemo.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import kotlinx.coroutines.launch

class DetailFragmentViewModel(item:NetworkItem, application: Application) : AndroidViewModel(application){

    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)

    //convert NetworkItem to FavoriteItem
    private val _selectedItem = item.asDomainItem()

    //    private val _isFavorite =MutableLiveData<Boolean>(true) khong nen de gia tri mac dinh
    private val _isFavorite =MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    //Chi check favorite 1 lan khi moi tao viewmodel
    init {
        checkFavorite()
    }

    fun onFavoriteClicking() {
        viewModelScope.launch {
            //if(favoriteItemRepository.getFavoriteItemById(_selectedItem.itemId)!= null){
            if(isFavorite.value == true){
                Log.d("Detail viewmodel","Item is added into Favorite")

                favoriteItemRepository.deleteFavoriteItem(_selectedItem.asFavoriteItem())
                //checkFavorite() không cần thiết
                _isFavorite.value = false

            }else
            {
                favoriteItemRepository.addFavoriteItem(_selectedItem.asFavoriteItem())

                //checkFavorite()
                _isFavorite.value = true
            }

        }
    }

    private fun checkFavorite() {
        viewModelScope.launch {
            _isFavorite.value =
                favoriteItemRepository.getFavoriteItemById(_selectedItem.itemId) !== null

        }
    }




    class Factory(
        private val item: NetworkItem,
        private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailFragmentViewModel::class.java)){
                return DetailFragmentViewModel( item, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}