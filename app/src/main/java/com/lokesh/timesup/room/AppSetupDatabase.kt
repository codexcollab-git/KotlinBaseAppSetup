package com.lokesh.timesup.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lokesh.timesup.room.entity.CategoriesEntity
import com.lokesh.timesup.room.entity.ProductEntity
import com.lokesh.timesup.ui.module.xmlview.categories.offline.dao.CategoryRoomDao
import com.lokesh.timesup.ui.module.xmlview.products.offline.queries.ProductRoomDao

@Database(
    entities = [ProductEntity::class, CategoriesEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppSetupDatabase : RoomDatabase() {
    abstract val tweetDao: ProductRoomDao
    abstract val categoryDao: CategoryRoomDao
}
