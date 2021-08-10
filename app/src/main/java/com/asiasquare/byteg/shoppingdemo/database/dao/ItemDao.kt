package com.asiasquare.byteg.shoppingdemo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asiasquare.byteg.shoppingdemo.database.items.LocalItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item : LocalItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items : List<LocalItem>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item : LocalItem)

    @Delete
    fun deleteItem(item : LocalItem)

    @Query(value = "DELETE FROM local_items_table")
    fun clearAllItems()

    @Query(value = "SELECT * FROM local_items_table")
    fun getAllItems() : LiveData<List<LocalItem>>

    @Query(value = "SELECT * FROM local_items_table WHERE item_brand_id = :catalogId")
    fun getAllItemsById(catalogId: Int) : LiveData<List<LocalItem>>

    @Query(value = "SELECT * FROM local_items_table WHERE item_brand_id = 0")
    fun getItemsIdFrist() : LiveData<List<LocalItem>>

    @Query("SELECT * FROM local_items_table WHERE item_name LIKE '%' || :searchQuery || '%' ORDER BY item_name DESC")
    fun getSearchItems(searchQuery: String): Flow<List<LocalItem>>

}