package com.asiasquare.byteg.shoppingdemo.payment.method

import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.dao.LocalCustomerDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentMethodViewModel(val database : LocalCustomerDatabaseDao) : ViewModel(){

    val paymentMethod = MutableLiveData<Int>()
    //Check if there is data on database
    private val _countCustomer = MutableLiveData<Int>()
    val countCustomer : LiveData<Int>
        get() = _countCustomer

    private val _navigateToInfo = MutableLiveData<Boolean>()
    val navigateToInfo : LiveData<Boolean>
        get() = _navigateToInfo

    private val _navigateToList = MutableLiveData<Boolean>()
    val navigateToList : LiveData<Boolean>
        get() = _navigateToList

    private val _navigateToCashPayment = MutableLiveData<Boolean>()
    val navigateToCashPayment : LiveData<Boolean>
        get() = _navigateToCashPayment

    init {
        _countCustomer.value = -1
        _navigateToInfo.value = false
        _navigateToList.value = false
        _navigateToCashPayment.value = false
        initCount()
    }

    fun navigateToInfo(){
        _navigateToInfo.value = true
        _navigateToList.value = false
        _navigateToCashPayment.value = false
    }
    fun navigateToList(){
        _navigateToInfo.value = false
        _navigateToList.value = true
        _navigateToCashPayment.value = false
    }

    fun navigateToCashPayment(){
        _navigateToInfo.value = false
        _navigateToList.value = false
        _navigateToCashPayment.value = true
    }



    private fun initCount() {
        viewModelScope.launch {
            _countCustomer.value = getCount()
        }
    }

    private suspend fun getCount(): Int? {
        return withContext(Dispatchers.IO){
            database.getCount()
        }
    }

    fun onNavigateComplete(){
        _navigateToInfo.value = false
        _navigateToList.value = false
        _navigateToCashPayment.value = false
    }


    class Factory(
        private var dataSource: LocalCustomerDatabaseDao
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaymentMethodViewModel::class.java)) {
                return PaymentMethodViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

    companion object {
        const val PAYMENT_CARD : Int = 0
        const val PAYMENT_CASH : Int = 1
    }
}