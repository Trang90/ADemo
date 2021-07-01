package com.asiasquare.byteg.shoppingdemo.database.customers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Domain model for Customer**/
@Parcelize
data class Customer(
    val customerId : String,
    val name : String,
    val email : String,
    val address: Address,
    val phone: String
): Parcelable{

    /** Convert Domain model to Local database customer **/
    fun asLocalCustomer() : LocalCustomer{
        return LocalCustomer(
            customerId = customerId,
            name = name,
            phoneNumber = phone,
            email = email,
            address = LocalAddress(
                city = address.city,
                country = address.country,
                addressLine = address.addressLine,
                postalCode = address.postalCode
            )
        )
    }
}

@Parcelize
data class Address(
    val city: String,
    val country: String,
    val addressLine: String,
    val postalCode: Int
) : Parcelable {
    override fun toString(): String {
        return "$addressLine, $postalCode $city."
    }
}