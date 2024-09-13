package com.lokesh.appsetup.ui.module.xmlview.categories.offline.queries

import com.lokesh.appsetup.room.entity.CategoriesEntity
import com.lokesh.appsetup.ui.module.xmlview.categories.offline.dao.CategoryRoomDao
import com.lokesh.appsetup.util.prefs.PrefUtils
import kotlinx.coroutines.flow.Flow

class CategoryEntries(private val dao: CategoryRoomDao, private val pref: PrefUtils) {

    /** ------------------------ */

    fun getCategories(): Flow<List<CategoriesEntity>?> {
        return dao.getCategories()
    }

    fun insertCategories(categories: List<CategoriesEntity>) {
        dao.clearCategories()
        dao.insertCategories(categories)
    }

    /** ------------------------ */
}
