package com.example.barcodescanner

import android.app.Application
import com.example.barcodescanner.data.AppDatabase
import com.example.barcodescanner.data.ScanRepository

class BarcodeScannerApp : Application() {
    lateinit var repository: ScanRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = ScanRepository(db.scanDao())
    }
}
