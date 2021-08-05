package com.asiasquare.byteg.shoppingdemo.database.items

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**Domain Model for Item**/
@Parcelize
data class Item(
    val itemId: Int,
    val itemName: String,
    val itemPrice: Double,
    val itemDiscountedPrice: Double,
    val itemImageSource: String,
    val itemWeight: String,
    val itemDescription: String,
    val itemBrand: String,
    val itemOrigin: String
) : Parcelable{
    fun asLocalItem() : LocalItem {
        return LocalItem(
            itemId = itemId,
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
    fun asFavoriteItem() : FavoriteItem{
        return FavoriteItem(
            itemId = itemId,
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


    fun asNetworkItem() : NetworkItem{
        return NetworkItem(
            itemId = itemId,
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