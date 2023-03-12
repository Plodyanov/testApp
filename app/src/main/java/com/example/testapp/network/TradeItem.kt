package com.example.testapp.network

import android.graphics.Color
import com.squareup.moshi.Json

data class TradeItem(
    val category: String,
    val name: String,
    val price: Float,
    val discount: Int = 0,
    @Json(name = "image_url")
    val imgSrcUrl: String
    )

data class Latest(
    @Json(name = "latest")
    val itemsList: List<TradeItem>
    )

data class FlashSale(
    @Json(name = "flash_sale")
    val itemsList: List<TradeItem>
    )

data class ItemDescription(
    val name: String,
    val description: String,
    val rating: Float,
    @Json(name = "number_of_reviews")
    val reviews: Int,
    val price: Float,
    val colors: List<String>,
    @Json(name = "image_urls")
    val imageUrls: List<String>
)