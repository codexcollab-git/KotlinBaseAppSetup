package com.lokesh.appsetup.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lokesh.appsetup.util.datastore.DataStoreUtils
import com.lokesh.appsetup.util.prefs.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var prefUtils: PrefUtils

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    internal val loading = MutableLiveData(false)
    internal var error = MutableLiveData<ErrorBody?>(null)

    /** Use this if you want to update UI with the api response */
    fun <T> setResponse(showLoader: Boolean, it: Result<T>, response: MutableLiveData<T?>) {
        when (it.status) {
            Result.Status.LOADING -> {
                if (showLoader) loading.value = true
            }
            Result.Status.SUCCESS -> {
                if (showLoader) loading.value = false
                response.value = it.data
            }
            Result.Status.ERROR -> {
                if (showLoader) loading.value = false
                error.value = ErrorBody(it.statusCode, it.message)
            }
        }
    }

    /** Use this if you do not want to update UI in case of any error */
    fun <T> setResponseWithoutError(showLoader: Boolean, it: Result<T>, response: MutableLiveData<T?>) {
        when (it.status) {
            Result.Status.LOADING -> {
                if (showLoader) loading.value = true
            }
            Result.Status.SUCCESS -> {
                if (showLoader) loading.value = false
                response.value = it.data
            }
            Result.Status.ERROR -> {
                if (showLoader) loading.value = false
                if (it.statusCode != null) error.value = ErrorBody(it.statusCode, it.message)
            }
        }
    }

    /** Use this if you do not want to update UI */
    fun <T> setResponseWithoutUIUpdate(showLoader: Boolean, it: Result<T>) {
        when (it.status) {
            Result.Status.LOADING -> {
                if (showLoader) loading.value = true
            }
            Result.Status.SUCCESS -> {
                if (showLoader) loading.value = false
            }
            Result.Status.ERROR -> {
                if (showLoader) loading.value = false
                if (it.statusCode != null) error.value = ErrorBody(it.statusCode, it.message)
            }
        }
    }

    private var apiCallJob: Job? = null

    fun background(cancelPrevious: Boolean = false, scope: CoroutineScope, function: suspend () -> Unit) {
        if (cancelPrevious) apiCallJob?.cancel()
        apiCallJob = scope.launch(Dispatchers.IO) { function.invoke() }
    }

    private var mainCallJob: Job? = null

    fun main(scope: CoroutineScope, function: suspend () -> Unit) {
        mainCallJob = scope.launch(Dispatchers.Main) { function.invoke() }
    }

    fun showLoading() {
        loading.value = true
    }

    fun hideLoading() {
        loading.value = false
    }
}
