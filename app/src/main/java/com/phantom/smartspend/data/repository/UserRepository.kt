package com.phantom.smartspend.data.repository

import com.phantom.smartspend.data.model.MonthlySpendingDto
import com.phantom.smartspend.network.ApiService
import com.phantom.smartspend.network.model.request.UpdateCurrencyRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.PieChartResponse
import com.phantom.smartspend.network.model.response.UpdateUserResponse
import com.phantom.smartspend.network.model.response.UserResponse
import com.phantom.smartspend.utils.DateUtils
import java.time.LocalDate

class UserRepository(
    private val api: ApiService
) {
    suspend fun getUserData(): UserResponse = api.getUserData()

    suspend fun updateUserData(request: UpdateUserRequest): UpdateUserResponse = api.updateUserData(request)
    suspend fun updatePreferredCurrency(request: UpdateCurrencyRequest): UpdateUserResponse = api.updatePreferredCurrency(request)

    suspend fun fetchMonthlySpending(
        from: LocalDate? = null,
        to: LocalDate? = null
    ): List<MonthlySpendingDto> {
        val fromR = from?.let(DateUtils::localDateToRfc3339Utc)
        val toR   = to?.let(DateUtils::localDateToRfc3339Utc)
        return api.getMonthlySpending(fromR, toR)
    }

    suspend fun getPieChart(from: String, to: String): PieChartResponse? {
        return api.getPieChart(from, to).data.firstOrNull()
    }
}