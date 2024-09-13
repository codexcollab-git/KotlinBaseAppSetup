package com.lokesh.appsetup.util.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.lokesh.appsetup.util.encryption.Encryption_Local
import kotlinx.coroutines.runBlocking

class PrefUtils(private val prefs: SharedPreferences) {

    private fun getEncryption(): Encryption_Local {
        val byteSize = ByteArray(16)
        return Encryption_Local.getDefault(
            PrefsKeys.ENCRYPTION_KEY.toString(),
            PrefsKeys.ENCRYPTION_SALT.toString(),
            byteSize
        )
    }

    var authToken: String?
        get() = String.format(
            "%s",
            getEncryption().decryptOrNull(prefs.getString(PrefsKeys.AUTH_TOKEN.toString(), null))
        )
        set(value) = prefs.edit().putString(PrefsKeys.AUTH_TOKEN.toString(), getEncryption().encryptOrNull(value)).apply()

    fun <T> putData(enum: PrefsKeys, value: T?) {
        val key = enum.toString()
        runBlocking {
            when (value) {
                is Int -> putIntData(key, value)
                is Long -> putLongData(key, value)
                is String -> putStringData(key, value)
                is Boolean -> putBooleanData(key, value)
                is Float -> putFloatData(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to the Prefs")
            }
        }
    }

    fun <T> putObjectData(enum: PrefsKeys, value: T) {
        runBlocking {
            putCustomData(enum.toString(), value)
        }
    }

    fun <T> getData(enum: PrefsKeys, defaultValue: T?): T {
        val key = enum.toString()
        val data = when (defaultValue) {
            is Int -> getIntData(key, defaultValue)
            is Long -> getLongData(key, defaultValue)
            is String -> getStringData(key, defaultValue)
            is Boolean -> getBooleanData(key, defaultValue)
            is Float -> getFloatData(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be fetched from Prefs")
        }
        return data as T
    }

    fun <T> getObjectData(enum: PrefsKeys, customClass: Class<T>): T? {
        return getCustomData(enum.toString(), customClass)
    }

    private fun putIntData(key: String, value: Int? = null) = prefs.edit().putInt(key, value ?: -1).apply()

    private fun putLongData(key: String, value: Long? = null) = prefs.edit().putLong(key, value ?: -1).apply()

    private fun putStringData(key: String, value: String? = null) = prefs.edit().putString(key, value).apply()

    private fun putBooleanData(key: String, value: Boolean? = null) = prefs.edit().putBoolean(key, value ?: false).apply()

    private fun putFloatData(key: String, value: Float? = null) = prefs.edit().putFloat(key, value ?: 0.0f).apply()

    private fun <T> putCustomData(key: String, value: T) {
        val data = toJson(value)
        putStringData(key, data)
    }

    private fun getIntData(key: String, value: Int? = null) = prefs.getInt(key, value ?: -1)

    private fun getLongData(key: String, value: Long? = null) = prefs.getLong(key, value ?: -1)

    private fun getStringData(key: String, value: String? = null) = prefs.getString(key, value)

    private fun getBooleanData(key: String, value: Boolean? = null) = prefs.getBoolean(key, value ?: false)

    private fun getFloatData(key: String, value: Float? = null) = prefs.getFloat(key, value ?: 0.0f)

    private fun <T> getCustomData(key: String, clazz: Class<T>): T? {
        val strData = getStringData(key, null)
        return fromJson(strData, clazz)
    }

    private fun <T> toJson(obj: T): String {
        return Gson().toJson(obj)
    }

    private fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if (json == null) return null
        return Gson().fromJson(json, clazz)
    }

    fun clearAll() = prefs.edit().clear().apply()

    fun clearSelected(args: ArrayList<PrefsKeys>) = prefs.edit().apply {
        args.map {
            val key = it.toString()
            this.remove(key)
        }
    }.commit()
}