package com.example.lunchtray.model

import com.example.lunchtray.db.DepositUiStateDBO


data class DepositUiState(
    val initialDeposit: Double = 0.0,
    val annualRate: Double = 0.0,
    val monthlyTopUp: Double = 0.0,
    val periodMonths: Int = 0,
    val finalAmount: Double = 0.0,
    val totalInterest: Double = 0.0
) {
    // Общая сумма вложенных средств
    val totalInvested: Double
        get() = initialDeposit + (monthlyTopUp * periodMonths)

    // Процент дохода от общей суммы вложений
    val profitPercentage: Double
        get() = if (totalInvested > 0) (totalInterest / totalInvested) * 100 else 0.0
}
fun DepositUiState.mapToDBO(uid: Int): DepositUiStateDBO {
    return DepositUiStateDBO(
        uid = uid,
        initialDeposit = this.initialDeposit,
        annualRate = this.annualRate,
        monthlyTopUp = this.monthlyTopUp,
        periodMonths = this.periodMonths.toDouble(), // преобразование Int в Double
        finalAmount = this.finalAmount,
        totalInterest = this.totalInterest
    )
}