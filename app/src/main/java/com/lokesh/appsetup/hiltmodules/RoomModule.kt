package com.lokesh.appsetup.hiltmodules

import androidx.room.Room
import com.lokesh.appsetup.application.App
import com.lokesh.appsetup.room.AppSetupDatabase
import com.lokesh.appsetup.ui.module.xmlview.categories.offline.queries.CategoryEntries
import com.lokesh.appsetup.ui.module.xmlview.products.offline.dao.ProductEntries
import com.lokesh.appsetup.util.prefs.PrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    companion object {
        const val ROOM_DB_NAME = "app_setup_db"
    }

    @Singleton
    @Provides
    fun provideDao(): AppSetupDatabase {
        return Room.databaseBuilder(App.getAppContext(), AppSetupDatabase::class.java, ROOM_DB_NAME)
            // .fallbackToDestructiveMigration()
            // .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideCategoryDB(db: AppSetupDatabase, prefUtils: PrefUtils): CategoryEntries {
        return CategoryEntries(db.categoryDao, prefUtils)
    }

    @Singleton
    @Provides
    fun provideTweetDB(db: AppSetupDatabase, prefUtils: PrefUtils): ProductEntries {
        return ProductEntries(db.productDao, prefUtils)
    }
}
