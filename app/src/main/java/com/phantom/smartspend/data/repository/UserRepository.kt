package com.phantom.smartspend.data.repository

import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UserResponse

class UserRepository(
    private val api: ApiService
) {
    suspend fun getUserData(): UserResponse = api.getUserData()

    suspend fun updateUserOnboarding(request: UpdateUserOnboardingRequest): UpdateUserResponse = api.updateUserOnboarding(request)


    suspend fun updateUserData(request: UpdateUserRequest): UpdateUserResponse = api.updateUserData(request)
}