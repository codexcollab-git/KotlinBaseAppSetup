package com.lokesh.timesup.ui.module.xmlview.categories.offline.queries

import com.lokesh.timesup.room.entity.CategoriesEntity
import com.lokesh.timesup.ui.module.xmlview.categories.offline.dao.CategoryRoomDao
import com.lokesh.timesup.util.prefs.PrefUtils
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
