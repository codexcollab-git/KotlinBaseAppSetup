package com.lokesh.appsetup.ui.module.xmlview.categories.usecase

import com.lokesh.appsetup.base.Result
import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.remote.ApiHelper
import com.lokesh.appsetup.remote.ApiService
import com.lokesh.appsetup.ui.module.xmlview.categories.offline.mapper.CategoriesMapper
import com.lokesh.appsetup.ui.module.xmlview.categories.offline.queries.CategoryEntries
import com.lokesh.appsetup.util.DEFAULT_DELAY
import com.lokesh.appsetup.util.OFFLINE_SUCCESS
import com.lokesh.appsetup.util.doInBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface CategoriesAPIRepository {
    fun getCategories(): Flow<Result<ArrayList<Categories>?>>
    fun insertCategories(categories: List<Categories>?)
    fun getCategoriesOffline(): Flow<Result<List<Categories>?>>
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

    override fun getCategoriesOffline(): Flow<Result<List<Categories>?>> {
        return flow {
            delay(DEFAULT_DELAY)
            emit(Result.loading())
            entries.getCategories().collect {
                emit(
                    Result.success(OFFLINE_SUCCESS, mapper.mapCategoriesEntityToCategories(it))
                )
            }
        }.flowOn(Dispatchers.IO)
    }
}
