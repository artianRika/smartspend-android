package com.phantom.smartspend.data.repository

import android.annotation.SuppressLint
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateCurrencyRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.MonthlyWrapper
import com.phantom.smartspend.network.model.response.PieChartResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UserResponse
import com.phantom.smartspend.network.model.response.MonthlySpendingDto
import com.phantom.smartspend.utils.DateUtils
import java.nio.file.attribute.FileTime.from
import java.time.LocalDate

class UserRepository(
    private val api: ApiService
) {
    suspend fun getUserData(): UserResponse = api.getUserData()

    suspend fun updateUserOnboarding(request: UpdateUserOnboardingRequest): UpdateUserResponse = api.updateUserOnboarding(request)


    suspend fun updateUserData(request: UpdateUserRequest): UpdateUserResponse = api.updateUserData(request)
    suspend fun updatePreferredCurrency(request: UpdateCurrencyRequest): UpdateUserResponse = api.updatePreferredCurrency(request)

    @SuppressLint("SuspiciousIndentation")
    suspend fun fetchMonthlySpending(
        from: String,
        to: String
    ): List<MonthlySpendingDto> {
    val wrapper = api.getMonthlySpending(from, to)
        return wrapper.data
    }

    suspend fun getPieChart(from: String, to: String): PieChartResponse? {
        return api.getPieChart(from, to).data.firstOrNull()
    }
}