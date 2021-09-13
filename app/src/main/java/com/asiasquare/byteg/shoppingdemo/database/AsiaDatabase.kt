package com.asiasquare.byteg.shoppingdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalCustomer
import com.asiasquare.byteg.shoppingdemo.database.customers.LocalOrder
import com.asiasquare.byteg.shoppingdemo.database.dao.*
import com.asiasquare.byteg.shoppingdemo.database.items.FavoriteItem
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import com.asiasquare.byteg.shoppingdemo.database.items.ShoppingBasketItem

@Database(entities = [LocalItem::class, ShoppingBasketItem::class, FavoriteItem::class, LocalCustomer::class, LocalOrder::class], version = 9,  exportSchema = false)
abstract class AsiaDatabase : RoomDatabase(){
    abstract val itemDao : ItemDao
    abstract val basketItemDao : ShoppingBasketItemDao
    abstract val favoriteItemDao : FavoriteItemDao
    abstract val localCustomerDatabaseDao : LocalCustomerDatabaseDao
    abstract val localOrderDao : LocalOrderDao

    companion object{
        @Volatile
        private var INSTANCE: AsiaDatabase?=null

        fun getInstance(context: Context) : AsiaDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsiaDatabase::class.java,
                        "asia_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}