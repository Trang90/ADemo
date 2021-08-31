package com.asiasquare.byteg.shoppingdemo.payment.newcustomer

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.asiasquare.byteg.shoppingdemo.backendservice.CustomerApi
import com.asiasquare.byteg.shoppingdemo.backendservice.CustomerRequest
import com.asiasquare.byteg.shoppingdemo.database.AsiaDatabase
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer
import com.asiasquare.byteg.shoppingdemo.database.customers.NetworkAddress
import com.asiasquare.byteg.shoppingdemo.database.customers.NetworkCustomer
import com.asiasquare.byteg.shoppingdemo.repository.CustomersRepository
import com.asiasquare.byteg.shoppingdemo.util.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewCustomerViewModel(application: Application):AndroidViewModel(application){


    private val database = AsiaDatabase.getInstance(application)
    private val customersRepository = CustomersRepository(database)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private val _response = MutableLiveData<LocalCustomer>()
    val response : LiveData<LocalCustomer>
        get() = _response

    private val _navigateToCustomerList = MutableLiveData<Boolean>()
    val navigateToCustomerList : LiveData<Boolean>
        get() = _navigateToCustomerList

    private val _haveAllInfo = MutableLiveData<Boolean>()
    val haveAllInfo : LiveData<Boolean>
        get() = _haveAllInfo

    fun updateInfo(info : Boolean){
        _haveAllInfo.value = info
    }

    fun onFinishCreateCustomer(){
        _haveAllInfo.value = false
    }

    fun createCustomer(
        name: String,
        email: String,
        phone: String,
        addressLine: String,
        addressCity: String,
        addressPostal: String
    ){
        _isLoading.value = true
        val customerRequest = createCustomerRequest(name,email,phone,addressLine,addressCity,addressPostal)
        if(isConnected(getApplication())){
            getCustomer(customerRequest)
        }else{
            Log.d("VCL INTERNET","????")
        }
    }

    private fun createCustomerRequest(
        name: String,
        email: String,
        phone: String,
        addressLine: String,
        addressCity: String,
        addressPostal: String
    ): CustomerRequest {
        val networkAddress = NetworkAddress(
            city = addressCity,
            country = "de",
            addressLine = addressLine,
            postalCode = addressPostal.toInt()
        )

        return CustomerRequest(
            name = name,
            email = email,
            address = networkAddress,
            phone = phone
        )
    }

    private fun getCustomer(customerRequest: CustomerRequest){
        viewModelScope.launch {
            try{
                var customer : NetworkCustomer
                withContext(Dispatchers.IO){
                    customer = CustomerApi.retrofitService.createNewCustomer(customerRequest)
                    customersRepository.addLocalCustomer(customer.asLocalCustomer())
                    Log.d("Get customer", "success")
                }
                _navigateToCustomerList.value = true
                _response.value = customer.asLocalCustomer()
            }catch (e: Exception){
                e.message?.let { Log.d("Get customer", it) }
            }
        }
    }

    fun onCompleteNavigateToCustomerList(){
        _navigateToCustomerList.value = false
        _isLoading.value = false
    }

    /**
     * Factory for constructing InfoViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewCustomerViewModel::class.java)){
                return NewCustomerViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}