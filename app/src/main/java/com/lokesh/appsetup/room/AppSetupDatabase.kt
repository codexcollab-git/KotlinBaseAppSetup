package com.lokesh.appsetup.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lokesh.appsetup.room.entity.CategoriesEntity
import com.lokesh.appsetup.room.entity.ProductEntity
import com.lokesh.appsetup.ui.module.xmlview.categories.offline.dao.CategoryRoomDao
import com.lokesh.appsetup.ui.module.xmlview.products.offline.queries.ProductRoomDao

@Database(
    entities = [ProductEntity::class, CategoriesEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppSetupDatabase : RoomDatabase() {
    abstract val productDao: ProductRoomDao
    abstract val categoryDao: CategoryRoomDao
}
