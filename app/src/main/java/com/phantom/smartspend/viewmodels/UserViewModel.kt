package com.phantom.smartspend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.model.MonthlySpendingDto
import com.phantom.smartspend.data.model.UserData
import com.phantom.smartspend.data.repository.UserRepository
import com.phantom.smartspend.network.model.request.UpdateCurrencyRequest
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import com.phantom.smartspend.network.model.response.PieChartResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class UserViewModel(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    fun setUserData(userData: UserData?) {
        _userData.value = userData
    }

    suspend fun getUserData(): UserData? {
        return try {
            val result = userRepo.getUserData()
            _userData.value = result.data
            result.data
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserOnboarding(
        balance: Float,
        savingGoal: Float,
        preferredCurrency: String,
    ) {
        try {
            userRepo.updateUserOnboarding(
                UpdateUserOnboardingRequest(
                    balance,
                    savingGoal,
                    preferredCurrency
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            val result = userRepo.getUserData()
            _userData.value = result.data
        }
    }

    suspend fun updateUserData(
        firstName: String,
        lastName: String,
        balance: Float,
        savingGoal: Float,
    ) {
        try {
            userRepo.updateUserData(
                UpdateUserRequest(
                    firstName,
                    lastName,
                    balance,
                    savingGoal
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            val result = userRepo.getUserData()
            _userData.value = result.data
        }
    }
    suspend fun updatePreferredCurrency(
        preferredCurrency: String,
    ) {
        try {
            userRepo.updatePreferredCurrency(
                UpdateCurrencyRequest(
                    preferredCurrency
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            val result = userRepo.getUserData()
            _userData.value = result.data
        }
    }
    //GET SPENDING DATA
    private val _monthlySpending = MutableStateFlow<List<MonthlySpendingDto>>(emptyList())
    val monthlySpending: StateFlow<List<MonthlySpendingDto>> = _monthlySpending
    /** Initial load (optional) */
    fun loadMonthlySpending(from: LocalDate? = null, to: LocalDate? = null) {
        viewModelScope.launch {
            try {
                _monthlySpending.value = userRepo.fetchMonthlySpending(from, to)
            } catch (_: Exception) {
                _monthlySpending.value = emptyList()
            }
        }
    }

    /** Pull-to-refresh or after date picker */
    fun refreshMonthlySpending(from: LocalDate? = null, to: LocalDate? = null) {
        loadMonthlySpending(from, to)
    }
//Get PIE DATA
private val _pieChart = MutableStateFlow<PieChartResponse?>(null)
    val pieChart: StateFlow<PieChartResponse?> = _pieChart

    fun loadPieChart(from: String, to: String) {
        viewModelScope.launch {
            try {
                val result = userRepo.getPieChart(from, to)
                println("📊 Pie chart result: $result")
                _pieChart.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _pieChart.value = null
            }
        }
    }


}