package com.lokesh.timesup.ui.module.xmlview.products.offline.dao

import com.lokesh.timesup.room.entity.ProductEntity
import com.lokesh.timesup.ui.module.xmlview.products.offline.queries.ProductRoomDao
import com.lokesh.timesup.util.prefs.PrefUtils
import kotlinx.coroutines.flow.Flow

class ProductEntries(private val dao: ProductRoomDao, private val pref: PrefUtils) {


    /** ------------------------ */

    fun getProductByCategories(category: String): Flow<List<ProductEntity>?> {
        return dao.getProductByCategories(category)
    }

    fun insertProducts(category: String, products: List<ProductEntity>) {
        dao.clearProductsByCategory(category)
        dao.insertProducts(products)
    }

    /** ------------------------ */

}
