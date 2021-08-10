package com.asiasquare.byteg.shoppingdemo.testpackage

//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.*
//import com.asiasquare.byteg.shoppingdemo.backendservice.ServerApi
//import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class BlankFragmentViewModel(application: Application):AndroidViewModel(application){
//
//    private val _size = MutableLiveData<Int>()
//    val size : LiveData<Int>
//        get() = _size
//
//    private val _text = MutableLiveData<NetworkItem>()
//    val text :LiveData<NetworkItem>
//        get() = _text
//
//    init {
//        getData()
//    }
//
//    private fun getData(){
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO){
//                    val listResult = ServerApi.retrofitService.getDataFirst()
//                    _size.postValue(listResult.size)
//                    _text.postValue(listResult[0])
//                }
//
//                Log.d("Get data","sucess")
//
//            }catch (e: Exception){
//                e.message?.let { Log.d("Get data",it) }
//            }
//        }
//    }
//
//
//
//
//
//
//
//    /**
//     * Factory for constructing InfoViewModel with parameter
//     */
//    class Factory(val app: Application) : ViewModelProvider.Factory{
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            if(modelClass.isAssignableFrom(BlankFragmentViewModel::class.java)){
//                return BlankFragmentViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//
//    }
//}

//ở CatalogFragment
//private val _catalog = MutableLiveData<Catalog?>()
//private val database = AsiaDatabase.getInstance(application)
//private val itemRepository = ItemRepository(database)
//
//private fun generateDummyList(){
//    val catalogList = mutableListOf<Catalog>()
//    //val catalog  = _catalog.value?.id!!
//
//    catalogList.add(Catalog(0,"Gạo & mì các loại", R.drawable.ct_bungao))
//    catalogList.add(Catalog(1,"Thực phẩm đông lạnh", R.drawable.ct_donglanh))
//    catalogList.add(Catalog(2,"Gia vị", R.drawable.ct_nuoccham))
//    catalogList.add(Catalog(3,"Rau, củ, quả", R.drawable.ct_raucu))
//    catalogList.add(Catalog(4,"Đồ khô & ăn vặt", R.drawable.ct_dokho))
//    catalogList.add(Catalog(5,"Thực phẩm đóng hộp", R.drawable.ct_dohop))

//        _catalogList.value= catalogList
//        getData(catalog)

//}

//    private fun getData(catalogId: Int){
//        viewModelScope.launch {
//            for (i in 0..5) {
//                 val items = itemRepository.getDataByCatalogId(catalogId)
//                itemRepository.addListLocalItem(items)
//            }
//        }
//    }

