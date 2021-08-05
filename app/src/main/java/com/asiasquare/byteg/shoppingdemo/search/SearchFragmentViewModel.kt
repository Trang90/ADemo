package com.asiasquare.byteg.shoppingdemo.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.dao.ItemDao
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class SearchFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)



    val searchQuery = MutableStateFlow("")
    private val searchFlow = searchQuery.flatMapLatest {
        database.itemDao.getSearchItems(it)
    }

    val searchItems = searchFlow.asLiveData()


    private val _navigateToDetail = MutableLiveData<NetworkItem?>()
    val navigateToDetail : MutableLiveData<NetworkItem?>
        get() = _navigateToDetail


    private val _isFavorite =MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    fun onDetailClick( item: LocalItem){
        _navigateToDetail.value = item.asDomainItem().asNetworkItem()
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }

    fun onFavoriteClicking(item: LocalItem) {

        viewModelScope.launch {

            if(isFavorite.value == true){
                Log.d("ItemList viewmodel","Item is added into Favorite")

                favoriteItemRepository.deleteFavoriteItem(item.asDomainItem().asFavoriteItem())
                _isFavorite.value = false

            }else
            {
                favoriteItemRepository.addFavoriteItem(item.asDomainItem().asFavoriteItem())

                _isFavorite.value = true
            }

        }
    }

    /**
     * Factory for constructing SearchViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SearchFragmentViewModel::class.java)){
                return SearchFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}