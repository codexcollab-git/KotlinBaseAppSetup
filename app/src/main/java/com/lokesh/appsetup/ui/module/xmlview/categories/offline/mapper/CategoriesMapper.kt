package com.lokesh.appsetup.ui.module.xmlview.categories.offline.mapper

import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.room.entity.CategoriesEntity

class CategoriesMapper {

    fun mapCategoriesToCategoriesEntity(list: List<Categories>?): List<CategoriesEntity> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map { CategoriesEntity(slug = it.slug ?: "", name = it.name ?: "") }
    }

    fun mapCategoriesEntityToCategories(list: List<CategoriesEntity>?): List<Categories> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map { Categories(slug = it.slug, name = it.name) }
    }
}