package com.asiasquare.byteg.shoppingdemo.database.customers

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**Customer data getting from the network via retrofit **/
@Parcelize
data class NetworkCustomer(
    @Json(name = "customer_id")
    val customerId : String,
    val name : String,
    val email : String,
    val address: NetworkAddress,
    val phone: String
) : Parcelable {

    fun asLocalCustomer(): LocalCustomer {
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
data class NetworkAddress(
    val city: String,
    val country: String,
    @Json(name = "line1")
    val addressLine: String,
    @Json(name = "postal_code")
    val postalCode: Int
) : Parcelable

fun List<NetworkCustomer>.asDomainModel() : List<Customer>{
    return map {
        Customer(
            customerId = it.customerId,
            name = it.name,
            phone = it.phone,
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

fun List<NetworkCustomer>.asLocalModel() : List<LocalCustomer>{
    return map {
        LocalCustomer(
            customerId = it.customerId,
            name = it.name,
            phoneNumber = it.phone,
            email = it.email,
            address = LocalAddress(
                city = it.address.city,
                country = it.address.country,
                addressLine = it.address.addressLine,
                postalCode = it.address.postalCode
            )
        )
    }
}