package com.asiasquare.byteg.shoppingdemo.payment.customerlist

import android.app.Application
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.customers.Customer
import com.asiasquare.byteg.shoppingdemo.repository.CustomersRepository
import kotlinx.coroutines.launch

class CustomerListViewModel(application: Application) : AndroidViewModel(application){


    private val database = AsiaDatabase.getInstance(application)
    private val customersRepository = CustomersRepository(database)

    private val _navigateToCustomer = MutableLiveData<Customer?>()
    val navigateToCustomer : LiveData<Customer?>
        get() = _navigateToCustomer

    private val _customers = MutableLiveData<List<Customer>>()
    val customer : LiveData<List<Customer>>
        get() = _customers

    val customerList = customersRepository.customers


    fun chooseCustomer(customer:Customer){
        _navigateToCustomer.value = customer

    }

    fun onCompleteNavigateToCustomer(){
        _navigateToCustomer.value = null
    }

    fun deleteCustomer(customer: Customer){
        viewModelScope.launch {
            customersRepository.deleteLocalCustomer(customer.asLocalCustomer())
        }
    }

    /**
     * Factory for constructing InfoViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CustomerListViewModel::class.java)){
                return CustomerListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}