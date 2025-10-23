package com.example.lunchtray.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DepositUiStateDAO {
    @Query("SELECT * FROM deposituistatedbo ")
    suspend fun getAll(): List<DepositUiStateDBO>

    @Insert
    suspend fun insert( users: DepositUiStateDBO)

    @Delete
    suspend fun delete(user: DepositUiStateDBO)

    @Query("DELETE FROM DepositUiStateDBO")
    suspend fun clearAll()

}