package com.lokesh.appsetup.ui.module.xmlview.products.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lokesh.appsetup.base.BaseViewModel
import com.lokesh.appsetup.base.Result
import com.lokesh.appsetup.model.Product
import com.lokesh.appsetup.ui.module.xmlview.products.model.GetProductsResponse
import com.lokesh.appsetup.ui.module.xmlview.products.usecase.ProductAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(app: Application, private val repo: ProductAPIRepository) : BaseViewModel(
    app
) {

    fun reset() {
        _getProducts.value = null
        _getProductsOffline.value = null
    }

    private val _getProducts: MutableLiveData<GetProductsResponse?> = MutableLiveData(null)
    val getProducts: LiveData<GetProductsResponse?> = _getProducts

    fun getProductsByCategory(showLoader: Boolean = false, category: String) {
        if (getProducts.value == null) {
            background(scope = viewModelScope) {
                repo.getProductsByCategory(category)
                    .onStart { emit(Result.loading()) }
                    .collect {
                        main(viewModelScope) { setResponse(showLoader, it, _getProducts) }
                        when (it.status) {
                            Result.Status.SUCCESS -> { repo.insertProducts(category, it.data?.products) }
                            else -> { /** Do Nothing */ }
                        }
                    }
            }
        }
    }

    private val _getProductsOffline: MutableLiveData<List<Product>?> = MutableLiveData(null)
    val getProductsOffline: LiveData<List<Product>?> = _getProductsOffline

    fun getProductsByCategoryOffline(showLoader: Boolean = false, category: String) {
        if (getProductsOffline.value == null) {
            background(true, viewModelScope) {
                repo.getProductsByCategoryOffline(category)
                    .onStart { emit(Result.loading()) }
                    .collect {
                        main(viewModelScope) { setResponse(showLoader, it, _getProductsOffline) }
                    }
            }
        }
    }
}