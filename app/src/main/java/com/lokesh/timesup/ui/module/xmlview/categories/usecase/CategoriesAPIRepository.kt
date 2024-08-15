package com.lokesh.timesup.ui.module.xmlview.categories.usecase

import com.lokesh.timesup.base.Result
import com.lokesh.timesup.model.Categories
import com.lokesh.timesup.remote.ApiHelper
import com.lokesh.timesup.remote.ApiService
import com.lokesh.timesup.ui.module.xmlview.categories.offline.mapper.CategoriesMapper
import com.lokesh.timesup.ui.module.xmlview.categories.offline.queries.CategoryEntries
import com.lokesh.timesup.util.DEFAULT_DELAY
import com.lokesh.timesup.util.OFFLINE_SUCCESS
import com.lokesh.timesup.util.doInBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface CategoriesAPIRepository {
    fun getCategories(): Flow<Result<ArrayList<Categories>?>>
    fun insertCategories(categories: List<Categories>?)
    fun getCategoriesOffline(): Flow<Result<List<String>?>>
}

class DefaultCategoriesRepo @Inject constructor(
    private val helper: ApiHelper,
    private val service: ApiService,
    private val entries: CategoryEntries,
    private val mapper: CategoriesMapper
) : CategoriesAPIRepository {

    override fun getCategories(): Flow<Result<ArrayList<Categories>?>> {
        return flow {
            emit(Result.loading())
            emit(helper.makeRequest { service.getCategories() })
        }.flowOn(Dispatchers.IO)
    }

    override fun insertCategories(categories: List<Categories>?) {
        doInBackground {
            entries.insertCategories(mapper.mapCategoriesToCategoriesEntity(categories))
        }
    }

    override fun getCategoriesOffline(): Flow<Result<List<String>?>> {
        return flow {
            delay(DEFAULT_DELAY)
            emit(Result.loading())
            entries.getCategories().collect { emit(Result.success(OFFLINE_SUCCESS, mapper.mapCategoriesEntityToCategories(it))) }
        }.flowOn(Dispatchers.IO)
    }
}
