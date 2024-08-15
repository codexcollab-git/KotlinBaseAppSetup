package com.lokesh.timesup.ui.module.xmlview.categories.offline.dao

import androidx.room.*
import com.lokesh.timesup.room.entity.CategoriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryRoomDao {

    /** ------------------------ */

    @Query("Select * from CategoriesEntity order by name asc")
    fun getCategories(): Flow<List<CategoriesEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<CategoriesEntity>)

    @Query("DELETE from CategoriesEntity")
    fun clearCategories()

    /** ------------------------ */
}
