package com.lokesh.timesup.ui.module.xmlview.categories.offline.mapper

import com.lokesh.timesup.model.Categories
import com.lokesh.timesup.room.entity.CategoriesEntity

class CategoriesMapper {

    fun mapCategoriesToCategoriesEntity(list: List<Categories>?): List<CategoriesEntity> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map { CategoriesEntity(name = it.name ?: "") }
    }

    fun mapCategoriesEntityToCategories(list: List<CategoriesEntity>?): List<String> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map { it.name }
    }
}