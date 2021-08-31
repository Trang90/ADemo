package com.asiasquare.byteg.shoppingdemo.database.customers

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_order_table")
data class LocalOrder(
    @PrimaryKey
    var orderId: String,
    @ColumnInfo(name = "order_time")
    var orderTime: String,
    @ColumnInfo(name = "order_total")
    var orderTotal: Int,
    @ColumnInfo(name = "customer_id")
    var customerId : String,
    @Embedded
    var address : LocalAddress,
    @ColumnInfo(name = "customer_phone_number")
    var phoneNumber : String,
    @ColumnInfo(name = "customer_email")
    var email : String
)