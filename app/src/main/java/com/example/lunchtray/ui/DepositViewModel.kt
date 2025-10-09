package com.example.lunchtray.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import com.example.lunchtray.model.DepositUiState

class DepositViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

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