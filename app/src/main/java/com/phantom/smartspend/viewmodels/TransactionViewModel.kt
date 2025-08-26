package com.phantom.smartspend.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.phantom.smartspend.data.model.Transaction
import com.phantom.smartspend.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {


    val list = mutableListOf(
        Transaction("ATM", 2600.0, "Income", "07-08-2025"),
        Transaction("Food", 120.0, "Expense", "19-08-2025"),
        Transaction("ATM", 500.0, "Income", "12-08-2025"),
        Transaction("RentRentRentRentRent", 1350.45, "Expense", "15-08-2025"),
        Transaction("ATMATMATMATMATM", 2400.0, "Income", "28-08-2025"),
        Transaction("FoodFoodFoodFoodFoodFoodFoodFood", 85.75, "Expense", "03-08-2025"),
        Transaction("Salary", 3200.0, "Income", "12-08-2025"),
        Transaction("Transport", 60.0, "Expense", "25-08-2025"),
        Transaction("Shopping", 220.5, "Expense", "01-07-2025"),
        Transaction("ATM", 500.0, "Income", "07-07-2025"),
        Transaction("EntertainmentEntertainmentEntertainmentEntertainment", 120.0, "Expense", "19-07-2025"),
        Transaction("Food", 150.0, "Expense", "02-08-2025"),
        Transaction("ATM", 2600.0, "Income", "07-08-2025"),
    )


    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    private val _transactions = MutableStateFlow(
        list.sortedByDescending { LocalDate.parse(it.date, formatter) }
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val transactions: StateFlow<List<Transaction>> = _transactions

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTransactions() {
        val sortedList = list.sortedByDescending {
            LocalDate.parse(it.date, formatter)
        }
        _transactions.value = sortedList
    }


//    fun loadTransactions() {
//        viewModelScope.launch {
//            try {
//                val result = repository.getTransactions()
//                _transactions.value = result
//            } catch (e: Exception) {
//                // handle error
//            }
//        }
//    }
}