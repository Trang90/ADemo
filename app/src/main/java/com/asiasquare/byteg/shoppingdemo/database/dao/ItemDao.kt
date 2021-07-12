package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.items.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    fun insert(item : LocalItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<LocalItem>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item : LocalItem)

    @Delete
    fun deleteItem(item : LocalItem)

    @Query(value = "DELETE FROM local_items_table")
    fun clearAllItems()

    @Query(value = "SELECT * FROM local_items_table")
    fun getAllItems() : Flow<List<LocalItem>>

    //search function

//    fun getTasks(query: String, sortOrder: SortOrder): Flow<List<Item>> =
//        when (sortOrder) {
//            SortOrder.BY_PRICE -> getTasksSortedByPrice(query)
//            SortOrder.BY_NAME -> getTasksSortedByName(query)
//        }

    @Query("SELECT * FROM local_items_table WHERE item_name LIKE '%' || :searchQuery || '%' ORDER BY item_name DESC")
    fun getTasks(searchQuery: String): Flow<List<LocalItem>>

//    @Query("SELECT * FROM local_items_table WHERE item_name LIKE '%' || :searchQuery || '%' ORDER BY item_name DESC")
//    fun getTasksSortedByName(searchQuery: String): Flow<List<LocalItem>>
//
//    @Query("SELECT * FROM local_items_table WHERE item_name LIKE '%' || :searchQuery || '%' ORDER BY item_price DESC")
//    fun getTasksSortedByPrice(searchQuery: String): Flow<List<LocalItem>>
}