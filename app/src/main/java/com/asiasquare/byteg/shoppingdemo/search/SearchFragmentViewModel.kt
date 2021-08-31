package com.asiasquare.byteg.shoppingdemo.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.itemlist.ListStatus
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import com.asiasquare.byteg.shoppingdemo.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class SearchFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)
    private val itemRepository = ItemRepository(database)

    val searchQuery = MutableStateFlow("")
    private val searchFlow = searchQuery.flatMapLatest {
        database.itemDao.getSearchItems(it)
    }

    val searchItems = searchFlow.asLiveData()


    private val _navigateToDetail = MutableLiveData<LocalItem?>()
    val navigateToDetail : MutableLiveData<LocalItem?>
        get() = _navigateToDetail


    private val _isFavorite =MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite


    init {
        //getData()
    }

//    private fun getData(){
//        viewModelScope.launch {
//            val items = itemRepository.getAllData()
//            saveDataToLocalDatabase(items)
//        }
//    }
//
//    private fun saveDataToLocalDatabase(items: List<NetworkItem>){
//        viewModelScope.launch {
//            try {
//                itemRepository.addListLocalItem(items)
//            }catch (e: Exception){
//                e.message?.let { Log.d("Search: Get all data",it) }
//            }
//        }
//
//    }

    fun onDetailClick( item: LocalItem){
        _navigateToDetail.value = item
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }

        fun onFavoriteClicking(item: LocalItem) {
        viewModelScope.launch {
//            _isFavorite.value =
//                favoriteItemRepository.getFavoriteItemById(item.asDomainItem().itemId) !== null
            val isCurrentFavorite =
                favoriteItemRepository.getFavoriteItemById(item.asDomainItem().itemId) !== null
            if(isCurrentFavorite){
                Log.d("Search viewmodel","Item is added into Favorite")

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