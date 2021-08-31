package com.asiasquare.byteg.shoppingdemo.payment.thanhtoantindung

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asiasquare.byteg.shoppingdemo.database.dao.ShoppingBasketItemDao

class ThanhToanTinDungViewModelFactory (
        private val dataSource: ShoppingBasketItemDao,
        private val application: Application
        ) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThanhToanTinDungViewModel::class.java)){
            return ThanhToanTinDungViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}