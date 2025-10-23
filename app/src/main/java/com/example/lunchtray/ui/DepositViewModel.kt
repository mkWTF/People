package com.example.lunchtray.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchtray.MyApplication
import com.example.lunchtray.db.DepositUiStateDBO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import com.example.lunchtray.model.DepositUiState
import com.example.lunchtray.model.mapToDBO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()
    private val userDao = MyApplication.getInstance().database.depositUiStateDAO()

    private val _users = MutableStateFlow<List<DepositUiStateDBO>>(emptyList())
    val users: StateFlow<List<DepositUiStateDBO>> = _users.asStateFlow()
    val numberedDeposits: StateFlow<List<Pair<DepositUiStateDBO, Int>>> =
        users.map { depositsList ->
            depositsList.sortedBy { it.uid }.mapIndexed { index, deposit ->
                deposit to (index + 1)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadUsers()
    }
    fun insertUser(user: DepositUiState) {
        viewModelScope.launch {
            val currentUsers = userDao.getAll()
            val maxUid = currentUsers.maxByOrNull { it.uid }?.uid ?: 0
            val newUid = maxUid + 1
            userDao.insert(user.mapToDBO(uid = newUid))
            loadUsers()
        }
    }
    private fun loadUsers() {
        viewModelScope.launch {
            _users.value = userDao.getAll()
        }
    }

    fun deleteDeposit(deposit: DepositUiStateDBO) {
        viewModelScope.launch {
            userDao.delete(deposit)
            loadUsers()
        }
    }

    fun clearAllDeposits() {
        viewModelScope.launch {
            userDao.clearAll()
            loadUsers()
        }
    }

    fun updateDepositParams(initialDeposit: Double, annualRate: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                initialDeposit = initialDeposit,
                annualRate = annualRate
            )
        }
    }

    fun updateMonthlyTopUp(monthlyTopUp: Double, periodMonths: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                monthlyTopUp = monthlyTopUp,
                periodMonths = periodMonths
            )
        }
    }

    fun calculateResults() {
        _uiState.update { currentState ->
            val monthlyRate = currentState.annualRate / 12 / 100
            var finalAmount = currentState.initialDeposit

            // Расчет с учетом ежемесячных пополнений и капитализации
            for (month in 1..currentState.periodMonths) {
                finalAmount += currentState.monthlyTopUp
                finalAmount *= (1 + monthlyRate)
            }

            val totalInvested = currentState.initialDeposit +
                    (currentState.monthlyTopUp * currentState.periodMonths)
            val totalInterest = finalAmount - totalInvested

            currentState.copy(
                finalAmount = finalAmount,
                totalInterest = totalInterest
            )
        }
    }

    fun resetDeposit() {
        _uiState.value = DepositUiState()
    }
}


fun Double.formatCurrency(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}