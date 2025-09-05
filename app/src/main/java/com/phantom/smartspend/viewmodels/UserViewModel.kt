package com.phantom.smartspend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.model.UserData
import com.phantom.smartspend.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
}