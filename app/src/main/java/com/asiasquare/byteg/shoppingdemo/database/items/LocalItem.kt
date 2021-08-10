package com.asiasquare.byteg.shoppingdemo.database.items

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "local_items_table")
@Parcelize
data class LocalItem(
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemId : Int,
    @ColumnInfo(name = "item_brand_id")
    var itemBrandId: Int,
    @ColumnInfo(name = "item_name")
    var itemName: String,
    @ColumnInfo(name = "item_price")
    var itemPrice : Double,
    @ColumnInfo(name = "item_discounted_price")
    var itemDiscountedPrice : Double,
    @ColumnInfo(name = "item_image_src")
    var itemImageSource : String,
    @ColumnInfo(name = "item_weight")
    var itemWeight : String,
    @ColumnInfo(name = "item_description")
    var itemDescription : String,
    @ColumnInfo(name = "item_brand")
    var itemBrand : String,
    @ColumnInfo(name = "item_origin")
    var itemOrigin : String
) : Parcelable {

    fun asDomainItem(): Item {
        return Item(
            itemId = itemId,
            itemBrandId = itemBrandId,
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