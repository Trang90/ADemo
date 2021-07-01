package com.asiasquare.byteg.shoppingdemo.itemlist

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList

class ItemListFragmentViewModel(application: Application) : AndroidViewModel(application){

    /**
     * List of catalog, observe this to get tha change in database
     */
    private val _itemList = MutableLiveData<List<ItemList>>()
    val itemList : LiveData<List<ItemList>>
        get() = _itemList

    private val _navigateToDetail = MutableLiveData<ItemList?>()
    val navigateToDetail : LiveData<ItemList?>
        get() = _navigateToDetail

    init {
        generateDummyList()
    }


    /**
     * Create dummy list for testing
     */
    private fun generateDummyList(){
        val itemList = mutableListOf<ItemList>()

        itemList.add(ItemList(0,"Gạo & mì các loại", R.drawable.ct_bungao))
        itemList.add(ItemList(1,"Thực phẩm đông lạnh", R.drawable.ct_donglanh))
        itemList.add(ItemList(2,"Gia vị", R.drawable.ct_nuoccham))
        itemList.add(ItemList(3,"Rau, củ, quả", R.drawable.ct_raucu))
        itemList.add(ItemList(4,"Đồ khô & ăn vặt", R.drawable.ct_dokho))
        itemList.add(ItemList(5,"Thực phẩm đóng hộp", R.drawable.ct_dohop))

        _itemList.value= itemList
    }


    fun onDetailClick( itemList: ItemList){
        _navigateToDetail.value = itemList
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }



    class Factory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ItemListFragmentViewModel::class.java)){
                return ItemListFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}