package com.lokesh.timesup.ui.module.xmlview.categories.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lokesh.timesup.base.BaseViewModel
import com.lokesh.timesup.base.Result
import com.lokesh.timesup.model.Categories
import com.lokesh.timesup.ui.module.xmlview.categories.usecase.CategoriesAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(app: Application, private val repo: CategoriesAPIRepository) : BaseViewModel(app) {

    fun reset() {
        _getCategories.value = null
        _getCategoriesOffline.value = null
    }

    private val _getCategories: MutableLiveData<ArrayList<Categories>?> = MutableLiveData(null)
    val getCategories: LiveData<ArrayList<Categories>?> = _getCategories

    fun getCategories(showLoader: Boolean = false) {
        if (getCategories.value == null) {
            background(scope = viewModelScope) {
                repo.getCategories()
                    .onStart { emit(Result.loading()) }
                    .collect {
                        main(viewModelScope) { setResponse(showLoader, it, _getCategories) }
                        when (it.status) {
                            Result.Status.SUCCESS -> { repo.insertCategories(it.data) }
                            else -> { /** Do Nothing */ }
                        }
                    }
            }
        }
    }

    private val _getCategoriesOffline: MutableLiveData<List<String>?> = MutableLiveData(null)
    val getCategoriesOffline: LiveData<List<String>?> = _getCategoriesOffline

    fun getCategoriesOffline(showLoader: Boolean = false) {
        if (getCategoriesOffline.value == null) {
            background(true, viewModelScope) {
                repo.getCategoriesOffline()
                    .onStart { emit(Result.loading()) }
                    .collect {
                        main(viewModelScope) { setResponse(showLoader, it, _getCategoriesOffline) }
                    }
            }
        }
    }
}