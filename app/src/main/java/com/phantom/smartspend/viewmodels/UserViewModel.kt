package com.phantom.smartspend.viewmodels

import androidx.lifecycle.ViewModel
import com.phantom.smartspend.data.model.UserData
import com.phantom.smartspend.data.repository.UserRepository
import com.phantom.smartspend.network.model.request.UpdateUserOnboardingRequest
import com.phantom.smartspend.network.model.request.UpdateUserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
}