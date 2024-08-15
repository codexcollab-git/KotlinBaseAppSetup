package com.lokesh.timesup.ui.module.xmlview.products.usecase

import com.lokesh.timesup.base.Result
import com.lokesh.timesup.model.Product
import com.lokesh.timesup.remote.ApiHelper
import com.lokesh.timesup.remote.ApiService
import com.lokesh.timesup.ui.module.xmlview.products.offline.mapper.ProductMapper
import com.lokesh.timesup.ui.module.xmlview.products.model.GetProductsResponse
import com.lokesh.timesup.ui.module.xmlview.products.offline.dao.ProductEntries
import com.lokesh.timesup.util.DEFAULT_DELAY
import com.lokesh.timesup.util.OFFLINE_SUCCESS
import com.lokesh.timesup.util.doInBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ProductAPIRepository {
    suspend fun getProductsByCategory(category: String): Flow<Result<GetProductsResponse?>>
    fun insertProducts(category:String, products: List<Product>?)
    fun getProductsByCategoryOffline(category:String): Flow<Result<List<Product>?>>
}

class DefaultProductRepo @Inject constructor(
    private val helper: ApiHelper,
    private val service: ApiService,
    private val entries: ProductEntries,
    private val mapper: ProductMapper
) : ProductAPIRepository {

    override suspend fun getProductsByCategory(category: String): Flow<Result<GetProductsResponse?>> {
        return flow {
            emit(Result.loading())
            emit(helper.makeRequest { service.getProductsByCategory(category) })
        }.flowOn(Dispatchers.IO)
    }

    override fun insertProducts(category:String, products: List<Product>?) {
        doInBackground {
            entries.insertProducts(category, mapper.mapProductToProductsEntity(products))
        }
    }

    override fun getProductsByCategoryOffline(category:String): Flow<Result<List<Product>?>> {
        return flow {
            delay(DEFAULT_DELAY)
            emit(Result.loading())
            entries.getProductByCategories(category).collect { emit(Result.success(OFFLINE_SUCCESS, mapper.mapProductsEntityEntityToProduct(it))) }
        }.flowOn(Dispatchers.IO)
    }
}
