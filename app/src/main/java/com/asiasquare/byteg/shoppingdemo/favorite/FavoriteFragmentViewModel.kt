package com.asiasquare.byteg.shoppingdemo.favorite

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList

class FavoriteFragmentViewModel (application: Application) : AndroidViewModel(application) {
    private val _favoriteList = MutableLiveData<List<ItemList>>()
    val favoriteList : LiveData<List<ItemList>>
        get() = _favoriteList

    init {
        generateDummyList()
    }


    /**
     * Create dummy list for testing
     */
    private fun generateDummyList(){
        val favoriteList = mutableListOf<ItemList>()

        favoriteList.add(ItemList(0,"Gạo & mì các loại", R.drawable.ct_bungao))
        favoriteList.add(ItemList(1,"Thực phẩm đông lạnh", R.drawable.ct_donglanh))
        favoriteList.add(ItemList(2,"Gia vị", R.drawable.ct_nuoccham))
        favoriteList.add(ItemList(3,"Rau, củ, quả", R.drawable.ct_raucu))
        favoriteList.add(ItemList(4,"Đồ khô & ăn vặt", R.drawable.ct_dokho))
        favoriteList.add(ItemList(5,"Thực phẩm đóng hộp", R.drawable.ct_dohop))

        _favoriteList.value= favoriteList
    }


    /**
     * Factory for constructing CatalogFragmentViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FavoriteFragmentViewModel::class.java)){
                return FavoriteFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}