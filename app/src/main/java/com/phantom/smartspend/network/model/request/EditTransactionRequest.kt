package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class EditTransactionRequest(
    val title: String,
    @SerializedName("price")
    val amount: Float,
    @SerializedName("date_made")
    val date: String,
    @SerializedName("category_id")
    val categoryId: Int?
)
