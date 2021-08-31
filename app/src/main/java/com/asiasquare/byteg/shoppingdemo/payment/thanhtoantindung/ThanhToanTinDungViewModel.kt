package com.asiasquare.byteg.shoppingdemo.payment.thanhtoantindung

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asiasquare.byteg.shoppingdemo.database.dao.ShoppingBasketItemDao
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem
import com.google.gson.Gson
import kotlin.math.roundToInt

class ThanhToanTinDungViewModel (
        val database: ShoppingBasketItemDao,
        application: Application
        ) : AndroidViewModel(application){

        private var gioHang: LiveData<MutableList<ShoppingBasketItem>> = database.getAllItemsInBasket()
        private var _tongTien = Transformations.map(gioHang){giohang ->
                tinhTongTien(giohang)
        }

        val tongTien : LiveData<Double>
                get() = _tongTien

        private val _loaded = Transformations.map(gioHang){gioHang ->
                gioHang.isNotEmpty()
        }
        val loaded : LiveData<Boolean>
                get() = _loaded

        private val _showProcessBar = MutableLiveData<Boolean>()
        val showProcessBar : LiveData<Boolean>
                get() = _showProcessBar


        /** Tinh tong tien. Mien phi van chuyen cho hoa don tren 50e **/
        private fun tinhTongTien(giohang: MutableList<ShoppingBasketItem>) : Double {
                var tongTien = 0.00
                for(item in giohang){
                        tongTien += item.itemPrice
                }
                if(tongTien<50){
                        tongTien+= Companion.TIEN_VAN_CHUYEN
                }
                return tongTien
        }

        /** Tạo Json string de dua qua server-side **/
        fun getJsonString(): String {
                val payMap = mutableMapOf<String,Any>()
                val itemMap = mutableMapOf<String,Any>()
                val itemList = mutableListOf<MutableMap<String,Any>>()
                payMap["currency"] = "eur"
                itemMap["id"] = "M0001"
                itemMap["price"] = getPrice()
                itemList.add(itemMap)
                payMap["items"] = itemList
                val gson = Gson()
                return gson.toJson(payMap)
        }

        private fun getPrice(): Int {
            val total : Double = _tongTien.value?: 5.00
            val result =  (total*100).roundToInt()
            Log.d("GIA TAG", result.toString())
            return result
        }


        companion object {
                private const val TIEN_VAN_CHUYEN = 5.99
        }

}