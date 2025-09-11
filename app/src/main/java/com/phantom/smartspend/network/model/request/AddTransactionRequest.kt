package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class AddTransactionRequest(
    val title: String,
    @SerializedName("price")
    val amount: Float,
    val type: String,
    @SerializedName("date_made")
    val date: String,
    @SerializedName("category_id")
    val categoryId: Int?
)
