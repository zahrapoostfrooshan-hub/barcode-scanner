package com.example.barcodescanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val format: String,
    val value: String,
    val timestamp: Long
)
