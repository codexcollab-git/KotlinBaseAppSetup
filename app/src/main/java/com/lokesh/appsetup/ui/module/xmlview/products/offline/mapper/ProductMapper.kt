package com.lokesh.appsetup.ui.module.xmlview.products.offline.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lokesh.appsetup.model.Product
import com.lokesh.appsetup.room.entity.ProductEntity

class ProductMapper {

    fun mapProductToProductsEntity(list: List<Product>?): List<ProductEntity> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map {
            var imageList: String? = null
            if (!it.images.isNullOrEmpty()) imageList = Gson().toJson(it.images)

            ProductEntity(
                id = it.id ?: 0, title = it.title, description = it.description, price = it.price, discountPercentage = it.discountPercentage,
                rating = it.rating, stock = it.stock, brand = it.brand, category = it.category ?: "", thumbnail = it.thumbnail, images = imageList
            )
        }
    }

    fun mapProductsEntityEntityToProduct(list: List<ProductEntity>?): List<Product> {
        if (list.isNullOrEmpty()) return arrayListOf()
        return list.map {
            val typeObj = object : TypeToken<ArrayList<String>>() {}.type
            var imageList: ArrayList<String> = arrayListOf()

            if (it.images != null && it.images != "") imageList = Gson().fromJson(it.images, typeObj)
            Product(
                id = it.id, title = it.title, description = it.description, price = it.price, discountPercentage = it.discountPercentage,
                rating = it.rating, stock = it.stock, brand = it.brand, category = it.category, thumbnail = it.thumbnail, images = imageList
            )
        }
    }
}