package com.lokesh.timesup.hiltmodules

import com.lokesh.timesup.ui.module.xmlview.categories.offline.mapper.CategoriesMapper
import com.lokesh.timesup.ui.module.xmlview.products.offline.mapper.ProductMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Singleton
    @Provides
    fun provideCategoriesMapper(): CategoriesMapper {
        return CategoriesMapper()
    }

    @Singleton
    @Provides
    fun provideProductMapper(): ProductMapper {
        return ProductMapper()
    }
}
