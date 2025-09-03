package com.phantom.smartspend.data.repository

import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.response.UserResponse

class UserRepository(
    private val api: ApiService
) {

    suspend fun getUserData(): UserResponse = api.getUserData()

}