package com.asiasquare.byteg.shoppingdemo.catalog

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.datamodel.Catalog

class CatalogFragmentViewModel(application: Application) : AndroidViewModel(application){

    /**
     * List of catalog, observe this to get the change in database
     */
    private val _catalogList = MutableLiveData<List<Catalog>>()
    val catalogList :LiveData<List<Catalog>>
        get() = _catalogList

    private val _navigateToCatalog = MutableLiveData<Catalog?>()
    val navigateToCatalog : LiveData<Catalog?>
        get() = _navigateToCatalog


    init {
        generateDummyList()
    }


    /**
     * Create dummy list for testing
     */
    private fun generateDummyList(){
        val catalogList = mutableListOf<Catalog>()

        catalogList.add(Catalog(0,"Gạo & mì các loại", R.drawable.ct_bungao))
        catalogList.add(Catalog(1,"Thực phẩm đông lạnh", R.drawable.ct_donglanh))
        catalogList.add(Catalog(2,"Gia vị", R.drawable.ct_nuoccham))
        catalogList.add(Catalog(3,"Rau, củ, quả", R.drawable.ct_raucu))
        catalogList.add(Catalog(4,"Đồ khô & ăn vặt", R.drawable.ct_dokho))
        catalogList.add(Catalog(5,"Thực phẩm đóng hộp", R.drawable.ct_dohop))

        _catalogList.value= catalogList
    }

    /** Pass catalog value when onClick **/
    fun onCatalogClick( catalog: Catalog){
        _navigateToCatalog.value = catalog
    }

    fun onNavigationComplete(){
        _navigateToCatalog.value = null
    }


    /**
     * Factory for constructing CatalogFragmentViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CatalogFragmentViewModel::class.java)){
                return CatalogFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}