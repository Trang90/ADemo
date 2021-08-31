package com.asiasquare.byteg.shoppingdemo.catalog

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.datamodel.Catalog
import com.asiasquare.byteg.shoppingdemo.repository.ItemRepository
import kotlinx.coroutines.launch

class CatalogFragmentViewModel(application: Application) : AndroidViewModel(application){
    private val database = AsiaDatabase.getInstance(application)
    private val itemRepository = ItemRepository(database)
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
        getData()
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

    /**
     * get all item for Search, call this function in Catalog instead of Search
     * to avoid displaying empty_view
     * when the list has not been retrieved
     */

    private fun getData(){
        viewModelScope.launch {
            val items = itemRepository.getAllData()
            saveDataToLocalDatabase(items)
        }
    }

    private fun saveDataToLocalDatabase(items: List<NetworkItem>){
        viewModelScope.launch {
            try {
                itemRepository.addListLocalItem(items)
            }catch (e: Exception){
                e.message?.let { Log.d("Search: Get all data",it) }
            }
        }

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