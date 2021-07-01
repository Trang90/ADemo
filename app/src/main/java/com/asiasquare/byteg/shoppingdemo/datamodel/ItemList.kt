package com.asiasquare.byteg.shoppingdemo.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ItemList(
    val id: Int,
    val textTenSanPham : String,
    val imgResource: Int

    //val textGiaSanPham: String,
    //val textGiaKhuyenMai: String
    ): Parcelable