package com.lokesh.appsetup.remote

import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.ui.module.xmlview.products.model.GetProductsResponse
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
