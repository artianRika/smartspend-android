package com.phantom.smartspend.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantom.smartspend.data.model.Transaction
import com.phantom.smartspend.data.repository.TransactionRepository
import com.phantom.smartspend.network.model.request.AddTransactionRequest
import com.phantom.smartspend.network.model.request.EditTransactionRequest
import com.phantom.smartspend.network.model.response.UploadImageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val response: UploadImageResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {


    private val _transactions = MutableStateFlow<List<Transaction>?> (emptyList())
    val transactions: StateFlow<List<Transaction>?> = _transactions


    // Refreshing state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTransactions() {
        viewModelScope.launch {
            _isRefreshing.value = true

            val result = repository.getTransactions()

            val sorted = result.data.sortedByDescending { OffsetDateTime.parse(it.date) }
            _transactions.value = sorted

            _isRefreshing.value = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTransaction(title: String, amount: Float, type: String, dateMade: String, categoryId: Int?){
        viewModelScope.launch {
            val response = repository.addTransaction(AddTransactionRequest(title, amount, type, dateMade, categoryId))
            getTransactions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun editTransaction(id: Int, title: String, amount: Float, dateMade: String, categoryId: Int?){
        viewModelScope.launch {
            val response = repository.editTransaction(id, EditTransactionRequest(title, amount, dateMade, categoryId))
            getTransactions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTransaction(id)
                getTransactions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    fun uploadReceipt(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                val response = repository.uploadImage(context, uri)
                _uploadState.value = UploadState.Success(response)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

}