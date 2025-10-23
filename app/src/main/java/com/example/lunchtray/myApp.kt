package com.example.lunchtray

import android.app.Application
import androidx.room.Room
import com.example.lunchtray.db.AppDatabase
class MyApplication(): Application() {
    companion object {
        private var instance: MyApplication? = null

        fun getInstance(): MyApplication = instance!!
    }
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}