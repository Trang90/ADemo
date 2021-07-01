package com.asiasquare.byteg.shoppingdemo.detail

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList

class DetailFragmentViewModel(itemList:ItemList, application: Application) : AndroidViewModel(application){

    /**
     * List of catalog, observe this to get tha change in database
     */
    private val _selectedItem = MutableLiveData<ItemList>()
    val selectedItem : LiveData<ItemList>
        get() = _selectedItem

    private val _navigateToPayment = MutableLiveData<ItemList?>()
    val navigateToPayment : LiveData<ItemList?>
        get() = _navigateToPayment

    init {
        _selectedItem.value = itemList
    }




    /**
     * Create dummy list for testing
     */
//    private fun generateDummyList(){
//        val detailList = mutableListOf<ItemList>()
//
//        detailList.add(ItemList(0,"Gạo & mì các loại", R.drawable.ct_bungao))
//        detailList.add(ItemList(1,"Thực phẩm đông lạnh", R.drawable.ct_donglanh))
//        detailList.add(ItemList(2,"Gia vị", R.drawable.ct_nuoccham))
//        detailList.add(ItemList(3,"Rau, củ, quả", R.drawable.ct_raucu))
//        detailList.add(ItemList(4,"Đồ khô & ăn vặt", R.drawable.ct_dokho))
//        detailList.add(ItemList(5,"Thực phẩm đóng hộp", R.drawable.ct_dohop))
//
//        _detailList.value= detailList
//    }


    fun onPaymentClick( item: ItemList){
        _navigateToPayment.value = item
    }

    fun onNavigationComplete(){
        _navigateToPayment.value = null
    }


    class Factory(
        private val itemProperty: ItemList,
        private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailFragmentViewModel::class.java)){
                return DetailFragmentViewModel(itemProperty, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}