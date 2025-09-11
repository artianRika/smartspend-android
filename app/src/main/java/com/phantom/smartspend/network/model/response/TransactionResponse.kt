package com.phantom.smartspend.network.model.response

import com.phantom.smartspend.data.model.Transaction

data class TransactionResponse(
    val data: List<Transaction>
)