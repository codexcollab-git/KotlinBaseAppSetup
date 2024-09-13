package com.lokesh.appsetup.hiltmodules

import com.lokesh.appsetup.ui.module.xmlview.categories.usecase.CategoriesAPIRepository
import com.lokesh.appsetup.ui.module.xmlview.categories.usecase.DefaultCategoriesRepo
import com.lokesh.appsetup.ui.module.xmlview.products.usecase.DefaultProductRepo
import com.lokesh.appsetup.ui.module.xmlview.products.usecase.ProductAPIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    fun providesCategoriesRepo(categoriesRepo: DefaultCategoriesRepo): CategoriesAPIRepository

    @Binds
    fun providesProductRepository(tweetsRepo: DefaultProductRepo): ProductAPIRepository
}
