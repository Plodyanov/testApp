package com.example.testapp

import android.app.Application
import com.example.testapp.database.AppDatabase
import com.example.testapp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

open class App: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(database.userDao()) }
}