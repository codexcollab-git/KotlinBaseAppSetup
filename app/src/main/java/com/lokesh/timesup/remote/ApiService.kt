package com.lokesh.timesup.remote

import com.lokesh.timesup.model.Categories
import com.lokesh.timesup.ui.module.xmlview.products.model.GetProductsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }

    @GET("products/categories")
    suspend fun getCategories(): Response<ArrayList<Categories>>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") name: String): Response<GetProductsResponse>
}
