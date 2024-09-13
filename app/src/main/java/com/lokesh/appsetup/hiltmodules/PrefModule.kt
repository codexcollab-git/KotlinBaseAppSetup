package com.lokesh.appsetup.hiltmodules

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.lokesh.appsetup.R
import com.lokesh.appsetup.application.App
import com.lokesh.appsetup.util.datastore.DataStoreUtils
import com.lokesh.appsetup.util.prefs.PrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefModule {

    /** ---- Shared Pref ---- */

    @Singleton
    @Provides
    fun provideSharedPreference(): SharedPreferences {
        return App.getAppContext().getSharedPreferences(
            "${App.getAppContext().getString(R.string.app_name)}_preferences",
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun providePrefUtils(sharedPref: SharedPreferences): PrefUtils {
        return PrefUtils(sharedPref)
    }

    /** ---- Data Store Pref ---- */

    private val Context.dataStore by preferencesDataStore(name = "data_store_pref")

    @Singleton
    @Provides
    fun provideDataStoreUtils(): DataStoreUtils {
        return DataStoreUtils(App.getAppContext().dataStore)
    }
}
