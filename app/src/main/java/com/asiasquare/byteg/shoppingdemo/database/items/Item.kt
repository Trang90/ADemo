package com.asiasquare.byteg.shoppingdemo.database.items

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**Domain Model for Item**/
@Parcelize
data class Item(
    val itemId: Int,
    val itemBrandId: Int,
    val itemName: String,
    val itemPrice: Double,
    val itemDiscountedPrice: Double = 0.0,
    val itemImageSource: String,
    val itemWeight: String,
    val itemDescription: String,
    val itemBrand: String,
    val itemOrigin: String
) : Parcelable{
    fun asLocalItem() : LocalItem {
        return LocalItem(
            itemId = itemId,
            itemBrandId = itemBrandId,
            itemName = itemName,
            itemPrice = itemPrice,
            itemDiscountedPrice = itemDiscountedPrice,
            itemImageSource = itemImageSource,
            itemWeight = itemWeight,
            itemDescription = itemDescription,
            itemBrand = itemBrand,
            itemOrigin = itemOrigin,
            itemFavorite = false
        )
    }
    fun asFavoriteItem() : FavoriteItem{
        return FavoriteItem(
            itemId = itemId,
            itemBrandId = itemBrandId,
            itemName = itemName,
            itemPrice = itemPrice,
            itemDiscountedPrice = itemDiscountedPrice,
            itemImageSource = itemImageSource,
            itemWeight = itemWeight,
            itemDescription = itemDescription,
            itemBrand = itemBrand,
            itemOrigin = itemOrigin
        )
    }

    fun asCartItem(amount: Int) : ShoppingBasketItem{
        return ShoppingBasketItem(
            itemId = itemId,
            itemBrandId = itemBrandId,
            itemName = itemName,
            itemPrice = itemPrice,
            itemDiscountedPrice = itemDiscountedPrice,
            itemImageSource = itemImageSource,
            itemWeight = itemWeight,
            itemDescription = itemDescription,
            itemBrand = itemBrand,
            itemOrigin = itemOrigin,
            itemAmount = amount,
            totalPrice = Math.round(amount * itemPrice * 100.0) / 100.0
        )
    }

    fun asNetworkItem() : NetworkItem{
        return NetworkItem(
            itemId = itemId,
            itemBrandId = itemBrandId,
            itemName = itemName,
            itemPrice = itemPrice,
            //itemDiscountedPrice = itemDiscountedPrice,
            itemImageSource = itemImageSource,
            itemWeight = itemWeight,
            itemDescription = itemDescription,
            itemBrand = itemBrand,
            itemOrigin = itemOrigin
        )
    }




}