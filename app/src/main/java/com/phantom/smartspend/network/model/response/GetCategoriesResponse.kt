package com.phantom.smartspend.network.model.response

import com.phantom.smartspend.data.model.Category

data class GetCategoriesResponse(
    val data: List<Category>
)
