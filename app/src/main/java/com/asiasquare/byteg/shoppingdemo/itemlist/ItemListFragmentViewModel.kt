package com.asiasquare.byteg.shoppingdemo.itemlist

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.backendservice.ServerApi
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.repository.FavoriteRepository
import com.asiasquare.byteg.shoppingdemo.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ListStatus { LOADING, ERROR, DONE }

class ItemListFragmentViewModel(application: Application, catalogId: Int) : AndroidViewModel(application){


    /**
     * List of catalog, observe this to get the change in database
     */


    private val _navigateToDetail = MutableLiveData<LocalItem?>()
    val navigateToDetail : LiveData<LocalItem?>
        get() = _navigateToDetail

    private val _list = MutableLiveData<List<NetworkItem>>()
    val list :LiveData<List<NetworkItem>>
        get() = _list

    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status


    private val database = AsiaDatabase.getInstance(application)
    private val favoriteItemRepository = FavoriteRepository(database)
    private val itemRepository = ItemRepository(database)

    val localItemList = itemRepository.getLocalItemListByCatalogId(catalogId)

    private val _isFavorite =MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    init {
        getData(catalogId)
    }

    private fun getData(catalogId: Int){
        viewModelScope.launch {
            _status.postValue (ListStatus.LOADING)

            val items = itemRepository.getDataByCatalogId(catalogId)

            _list.postValue(items)

            _status.postValue (ListStatus.DONE)

            saveDataToLocalDatabase(items)
        }
    }

    private fun saveDataToLocalDatabase(items: List<NetworkItem>){
        viewModelScope.launch {
            try {

                itemRepository.addListLocalItem(items)

            }catch (e: Exception){

                _status.postValue(ListStatus.ERROR)

            }
        }

    }


//    private fun getData(catalogId: Int){
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO){
//                    _status.postValue (ListStatus.LOADING)
//                    val listResult = when(catalogId){
//                        0-> ServerApi.retrofitService.getDataFirst()
//                        1-> ServerApi.retrofitService.getDataSecond()
//                        2-> ServerApi.retrofitService.getDataThird()
//                        3-> ServerApi.retrofitService.getDataFourth()
//                        4-> ServerApi.retrofitService.getDataFifth()
//                        5-> ServerApi.retrofitService.getDataSixth()
//                        else -> ServerApi.retrofitService.getDataSeventh()
//                    }
//
//                    _status.postValue (ListStatus.DONE)
//                    _list.postValue(listResult)
//
//                }
//
//                Log.d("Get data $catalogId","sucess")
//            }catch (e: Exception){
//                _status.postValue (ListStatus.ERROR)
//
//                e.message?.let { Log.d("Get data $catalogId",it) }
//            }
//        }
//    }

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



    fun onDetailClick( item: LocalItem){
        _navigateToDetail.value = item
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }


    class Factory(
        private val app: Application, private val catalogId: Int) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ItemListFragmentViewModel::class.java)){
                return ItemListFragmentViewModel(app,catalogId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}