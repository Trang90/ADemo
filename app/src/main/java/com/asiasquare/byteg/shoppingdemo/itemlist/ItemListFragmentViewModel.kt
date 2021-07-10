package com.asiasquare.byteg.shoppingdemo.itemlist

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.R
import com.asiasquare.byteg.shoppingdemo.backendservice.ServerApi
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import com.asiasquare.byteg.shoppingdemo.datamodel.ItemList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ListStatus { LOADING, ERROR, DONE }

class ItemListFragmentViewModel(application: Application, catalogId: Int) : AndroidViewModel(application){


    /**
     * List of catalog, observe this to get the change in database
     */

    private val _navigateToDetail = MutableLiveData<NetworkItem?>()
    val navigateToDetail : LiveData<NetworkItem?>
        get() = _navigateToDetail

    private val _text = MutableLiveData<List<NetworkItem>>()
    val text :LiveData<List<NetworkItem>>
        get() = _text

    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status

    init {
        getData(catalogId)
    }



    private fun getData(catalogId: Int){
        viewModelScope.launch {
            try {

                withContext(Dispatchers.IO){
                    _status.postValue (ListStatus.LOADING)
                    val listResult = when(catalogId){
                        0-> ServerApi.retrofitService.getDataFirst()
                        1-> ServerApi.retrofitService.getDataSecond()
                        2-> ServerApi.retrofitService.getDataThird()
                        3-> ServerApi.retrofitService.getDataFourth()
                        4-> ServerApi.retrofitService.getDataFifth()
                        5-> ServerApi.retrofitService.getDataSixth()
                        else -> ServerApi.retrofitService.getDataSeventh()
                    }

                    _status.postValue (ListStatus.DONE)
                    _text.postValue(listResult)
                }

                Log.d("Get data $catalogId","sucess")
            }catch (e: Exception){
                _status.postValue (ListStatus.ERROR)

                e.message?.let { Log.d("Get data $catalogId",it) }
            }
        }
    }

//    fun bindStatus(statusImageView: ImageView, status: ListStatus?) {
//        when (status) {
//            ListStatus.LOADING -> {
//                statusImageView.visibility = View.VISIBLE
//                statusImageView.setImageResource(R.drawable.loading_animation)
//            }
//            ListStatus.ERROR -> {
//                statusImageView.visibility = View.VISIBLE
//                statusImageView.setImageResource(R.drawable.ic_connection_error)
//            }
//            ListStatus.DONE -> {
//                statusImageView.visibility = View.GONE
//            }
//        }
//    }

    fun onDetailClick( itemList: NetworkItem){
        _navigateToDetail.value = itemList
    }

    fun onNavigationComplete(){
        _navigateToDetail.value = null
    }


    class Factory(private val app: Application, private val catalogId: Int) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ItemListFragmentViewModel::class.java)){
                return ItemListFragmentViewModel(app,catalogId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}