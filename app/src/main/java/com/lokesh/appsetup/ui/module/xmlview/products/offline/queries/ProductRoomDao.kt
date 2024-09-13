package com.lokesh.appsetup.ui.module.xmlview.products.offline.queries

import androidx.room.*
import com.lokesh.appsetup.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductRoomDao {

    /** ------------------------ */

    @Query("Select * from ProductEntity where category=:cat")
    fun getProductByCategories(cat: String): Flow<List<ProductEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE from ProductEntity where category=:cat")
    fun clearProductsByCategory(cat: String)

    /** ------------------------ */
}
