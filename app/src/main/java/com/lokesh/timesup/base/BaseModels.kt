package com.lokesh.timesup.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.ResponseBody

@Parcelize
data class OptionModel(
    var option_id: String = "",
    var head: String = "",
    var sub_head: String? = null,
    var extra_value: String? = null,
    var toggle: Boolean? = false,
    var entity_count: Int = 0
) : Parcelable

data class ErrorBody(
    var statusCode: Int? = null,
    var message: String? = null,
)

data class Result<out T>(
    val status: Status,
    val statusCode: Int? = null,
    val data: T?,
    val error: ResponseBody? = null,
    val message: String?
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
    companion object {
        fun <T> success(statusCode: Int?, data: T?): Result<T> {
            return Result(Status.SUCCESS, statusCode, data, null, null)
        }

        fun <T> error(statusCode: Int?, error: ResponseBody?, message: String): Result<T> {
            return Result(Status.ERROR, statusCode, null, error, message)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, null, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}
