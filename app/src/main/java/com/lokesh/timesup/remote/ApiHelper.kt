package com.lokesh.timesup.remote

import android.accounts.NetworkErrorException
import android.app.Activity
import com.lokesh.timesup.base.Result
import android.util.Log
import com.lokesh.timesup.R
import com.lokesh.timesup.application.App
import com.lokesh.timesup.ui.module.compose.MainActivity
import com.lokesh.timesup.util.NetworkManager
import com.lokesh.timesup.util.getStringResource
import com.lokesh.timesup.util.showToast
import com.lokesh.timesup.util.startActivityWithParams
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

class ApiHelper {

    suspend fun <T> makeRequest(
        request: suspend () -> Response<T>
    ): Result<T?> {
        if (!NetworkManager.internetAvailable) {
            return Result.error(null, null, getStringResource(R.string.no_int_connection))
        } else {
            return try {
                val result = request.invoke()
                if (result.isSuccessful) {
                    /** This handles >=200 to <=300 codes */
                    return Result.success(result.code(), result.body())
                } else {
                    Result.error(
                        result.code(),
                        result.errorBody(),
                        handleError(result.code(), result.errorBody())
                    )
                }
            } catch (e: Throwable) {
                Result.error(null, null, getErrorOrShowMessage(e))
            }
        }
    }

    private fun getErrorOrShowMessage(
        error: Throwable
    ): String {
        Log.e(getStringResource(R.string.app_name), error.localizedMessage, error)
        val message = when (error) {
            is NetworkErrorException -> getStringResource(R.string.no_int_connection)
            is ParseException -> getStringResource(R.string.parsing_error)
            is TimeoutException -> getStringResource(R.string.slow_internet)
            is SocketTimeoutException -> getStringResource(R.string.socket_timeout_server)
            is UnknownHostException -> getStringResource(R.string.could_not_reach_server)
            is ConnectException -> getStringResource(R.string.no_int_connection)
            else -> getStringResource(R.string.api_error)
        }
        return message
    }

    private fun handleError(code: Int, errorBody: ResponseBody?): String {
        var errorMessage = ""
        when (code) {
            500 -> errorMessage = getStringResource(R.string.internal_server_error)
            401 -> {
                if (App.getCurrentActivity() != null) {
                    errorMessage = getStringResource(R.string.auth_error_logging_out)
                    (App.getCurrentActivity() as Activity).finishAffinity()
                    /* GlobalScope.launch { Room.databaseBuilder(App.getAppContext(), StarboardDatabase::class.java, "starboard_db").build().clearAllTables() }
                    App.getAppContext().getSharedPreferences("${App.getAppContext().getString(R.string.app_name)}_preferences", 0)?.edit()?.clear()?.commit() */
                    App.getAppContext().startActivityWithParams(MainActivity::class.java, isNewTask = true)
                } else App.getAppContext().showToast(getStringResource(R.string.activity_not_found))
            }
            404 -> errorMessage = getStringResource(R.string.api_not_found)
            else -> {
                try {
                    val finalStringBuilder = StringBuilder()
                    val stringBuilder = StringBuilder()
                    var heading = ""
                    val jsonObject: JSONObject?
                    var jsonObjectInside: JSONObject?
                    try {
                        if (errorBody != null) {
                            jsonObject = JSONObject(errorBody.string())
                            val iterator: Iterator<String> = jsonObject.keys()
                            while (iterator.hasNext()) {
                                val key = iterator.next()
                                try {
                                    if (key.equals("message", ignoreCase = true)) {
                                        heading = jsonObject.getString(key)
                                    }
                                    if (key.equals("error", ignoreCase = true)) {
                                        val errMsg = jsonObject.getString(key)
                                        val json = JSONTokener(errMsg).nextValue()
                                        if (json is JSONObject) {
                                            jsonObjectInside = JSONObject(errMsg)
                                            val iteratorInside: Iterator<String> = jsonObjectInside.keys()
                                            while (iteratorInside.hasNext()) {
                                                val key2 = iteratorInside.next()
                                                val insideErrMsg = jsonObjectInside.getString(key2)
                                                stringBuilder.append(insideErrMsg + "\n")
                                            }
                                        } else if (json is JSONArray) {
                                            jsonObjectInside = json.getJSONObject(0)
                                            val iteratorInside: Iterator<String> =
                                                jsonObjectInside.keys()
                                            while (iteratorInside.hasNext()) {
                                                val key2 = iteratorInside.next()
                                                if (key2.equals("message", ignoreCase = true) || key2.equals("field", ignoreCase = true)) {
                                                    val insideErrMsg =
                                                        jsonObjectInside.getString(key2)
                                                    stringBuilder.append(insideErrMsg + "\n")
                                                }
                                            }
                                        }
                                    }
                                } catch (e: JSONException) {
                                    errorMessage = getStringResource(R.string.api_error)
                                }
                            }
                            if (heading != "") {
                                // finalStringBuilder.append("$heading\n")
                                val errMsg = stringBuilder.toString().trim()
                                if (errMsg == "") {
                                    finalStringBuilder.append("$heading\n")
                                } else {
                                    finalStringBuilder.append(errMsg)
                                }
                                errorMessage = finalStringBuilder.toString().trim()
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = getErrorOrShowMessage(e)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = getStringResource(R.string.api_error)
                }
            }
        }
        return errorMessage
    }
}
