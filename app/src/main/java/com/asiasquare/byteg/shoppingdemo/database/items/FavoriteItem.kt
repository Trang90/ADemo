package com.asiasquare.byteg.shoppingdemo.database.items

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_table")
@Parcelize
data class FavoriteItem (
    @PrimaryKey
    var itemId : Int = 0,
    @ColumnInfo(name = "item_brand_id")
    var itemBrandId: Int = 0,
    @ColumnInfo(name = "item_name")
    var itemName: String = "",
    @ColumnInfo(name = "item_price")
    var itemPrice : Double= 0.0,
    @ColumnInfo(name = "item_discounted_price")
    var itemDiscountedPrice : Double= 0.0,
    @ColumnInfo(name = "item_image_src")
    var itemImageSource : String= "",
    @ColumnInfo(name = "item_weight")
    var itemWeight : String= "",
    @ColumnInfo(name = "item_description")
    var itemDescription : String= "",
    @ColumnInfo(name = "item_brand")
    var itemBrand : String= "",
    @ColumnInfo(name = "item_origin")
    var itemOrigin : String= ""
) : Parcelable {

    fun asDomainItem() : Item {
        return Item(
            itemId = itemId,
            itemName = itemName,
            itemPrice = itemPrice,
            itemDiscountedPrice = 0.00,
            itemImageSource = itemImageSource,
            itemWeight = itemWeight,
            itemDescription = itemDescription,
            itemBrand = itemBrand,
            itemOrigin = itemOrigin
        )
    }
}