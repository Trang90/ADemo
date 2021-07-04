package com.asiasquare.byteg.shoppingdemo.testpackage

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.backendservice.ServerApi
import com.asiasquare.byteg.shoppingdemo.database.items.NetworkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlankFragmentViewModel(application: Application):AndroidViewModel(application){

    private val _size = MutableLiveData<Int>()
    val size : LiveData<Int>
        get() = _size

    private val _text = MutableLiveData<NetworkItem>()
    val text :LiveData<NetworkItem>
        get() = _text

    init {
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    val listResult = ServerApi.retrofitService.getDataFirst()
                    _size.postValue(listResult.size)
                    _text.postValue(listResult[0])
                }

                Log.d("Get data","sucess")

            }catch (e: Exception){
                e.message?.let { Log.d("Get data",it) }
            }
        }
    }







    /**
     * Factory for constructing InfoViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(BlankFragmentViewModel::class.java)){
                return BlankFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}