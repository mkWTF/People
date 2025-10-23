package com.example.lunchtray.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DepositUiStateDBO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositUiStateDAO(): DepositUiStateDAO
}