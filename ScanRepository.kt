package com.example.barcodescanner.data

import kotlinx.coroutines.flow.Flow

class ScanRepository(private val dao: ScanDao) {
    val history: Flow<List<ScanRecord>> = dao.getAll()

    suspend fun insert(record: ScanRecord) = dao.insert(record)
    suspend fun delete(record: ScanRecord) = dao.delete(record)
    suspend fun deleteAll() = dao.deleteAll()
}
