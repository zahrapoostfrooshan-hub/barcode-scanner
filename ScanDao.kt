package com.example.barcodescanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ScanRecord>>

    @Insert
    suspend fun insert(record: ScanRecord): Long

    @Delete
    suspend fun delete(record: ScanRecord)

    @Query("DELETE FROM scan_history")
    suspend fun deleteAll()
}
