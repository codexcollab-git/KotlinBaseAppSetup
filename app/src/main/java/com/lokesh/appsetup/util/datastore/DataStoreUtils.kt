package com.lokesh.appsetup.util.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStoreUtils(private val dataStore: DataStore<Preferences>) {

    fun <T> putData(enum: DataStoreKeys, value: T) {
        val key = enum.toString()
        runBlocking {
            when (value) {
                is Int -> putIntData(key, value)
                is Long -> putLongData(key, value)
                is String -> putStringData(key, value)
                is Boolean -> putBooleanData(key, value)
                is Float -> putFloatData(key, value)
                is Double -> putDoubleData(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
            }
        }
    }

    fun <T> putObjectData(enum: DataStoreKeys, value: T) {
        runBlocking {
            putCustomData(enum.toString(), value)
        }
    }

    fun <T> getData(enum: DataStoreKeys, defaultValue: T): T {
        val key = enum.toString()
        val data = when (defaultValue) {
            is Int -> getIntData(key, defaultValue)
            is Long -> getLongData(key, defaultValue)
            is String -> getStringData(key, defaultValue)
            is Boolean -> getBooleanData(key, defaultValue)
            is Float -> getFloatData(key, defaultValue)
            is Double -> getDoubleData(key, defaultValue)
            else -> throw IllegalArgumentException("This type cannot be fetched from the Data Store")
        }
        return data as T
    }

    fun <T> getObjectData(enum: DataStoreKeys, customClass: Class<T>): T? {
        return getCustomData(enum.toString(), customClass)
    }

    fun clearData() = runBlocking { dataStore.edit { it.clear() } }

    private suspend fun putIntData(key: String, value: Int) = dataStore.edit {
        it[intPreferencesKey(key)] = value
    }

    private suspend fun putLongData(key: String, value: Long) = dataStore.edit {
        it[longPreferencesKey(key)] = value
    }

    private suspend fun putStringData(key: String, value: String) = dataStore.edit {
        it[stringPreferencesKey(key)] = value
    }

    private suspend fun putBooleanData(key: String, value: Boolean) = dataStore.edit {
        it[booleanPreferencesKey(key)] = value
    }

    private suspend fun putFloatData(key: String, value: Float) = dataStore.edit {
        it[floatPreferencesKey(key)] = value
    }

    private suspend fun putDoubleData(key: String, value: Double) = dataStore.edit {
        it[doublePreferencesKey(key)] = value
    }

    private suspend fun <T> putCustomData(key: String, value: T) {
        val data = toJson(value)
        putStringData(key, data)
    }

    private fun getIntData(key: String, default: Int = 0): Int? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[intPreferencesKey(key)] ?: default
        }.firstOrNull()
    }

    private fun getLongData(key: String, default: Long = 0): Long? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[longPreferencesKey(key)] ?: default
        }.firstOrNull()
    }

    private fun getStringData(key: String, default: String? = null): String? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[stringPreferencesKey(key)] ?: default
        }.firstOrNull()
    }

    private fun getBooleanData(key: String, default: Boolean = false): Boolean? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: default
        }.firstOrNull()
    }

    private fun getFloatData(key: String, default: Float = 0.0f): Float? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[floatPreferencesKey(key)] ?: default
        }.firstOrNull()
    }

    private fun getDoubleData(key: String, default: Double = 0.00): Double? = runBlocking {
        return@runBlocking dataStore.data.map {
            it[doublePreferencesKey(key)] ?: default
        }.firstOrNull()
    }

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
}