package com.asiasquare.byteg.shoppingdemo.database.customers

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Database customer
 */
@Entity(tableName = "local_customer_table")
data class LocalCustomer(
    @PrimaryKey
    var customerId: String,
    @ColumnInfo(name = "customer_name")
    var name : String,
    @Embedded
    var address : LocalAddress,
    @ColumnInfo(name = "customer_phone_number")
    var phoneNumber : String,
    @ColumnInfo(name = "customer_email")
    var email : String
)

data class LocalAddress(
    @ColumnInfo(name = "customer_city")
    val city: String,
    @ColumnInfo(name = "customer_country")
    val country: String,
    @ColumnInfo(name = "customer_address_line")
    val addressLine: String,
    @ColumnInfo(name = "customer_postal_code")
    val postalCode: Int
)

fun List<LocalCustomer>.asDomainModel() : List<Customer>{
    return map {
        Customer(
            customerId = it.customerId,
            name = it.name,
            phone = it.phoneNumber,
            email = it.email,
            address = Address(
                city = it.address.city,
                country = it.address.country,
                addressLine = it.address.addressLine,
                postalCode = it.address.postalCode
            )
        )
    }
}