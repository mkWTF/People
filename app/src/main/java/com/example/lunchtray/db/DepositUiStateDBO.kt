package com.example.lunchtray.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DepositUiStateDBO(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "initial_Deposit") val initialDeposit: Double = 0.0,
    @ColumnInfo(name = "annualRate") val annualRate: Double = 0.0,
    @ColumnInfo(name = "monthlyTopUp") val monthlyTopUp: Double = 0.0,
    @ColumnInfo(name = "periodMonths") val periodMonths: Double = 0.0,
    @ColumnInfo(name = "finalAmount") val finalAmount: Double = 0.0,
    @ColumnInfo(name = "totalInterest") val totalInterest: Double = 0.0
    )
