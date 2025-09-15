package com.phantom.smartspend.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateCurrencyRequest(
    @SerializedName("preferred_currency")
    val preferredCurrency: String,
)