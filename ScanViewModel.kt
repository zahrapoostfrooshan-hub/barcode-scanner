package com.example.barcodescanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.barcodescanner.data.ScanRecord
import com.example.barcodescanner.data.ScanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScanUiResult(
    val format: String,
    val value: String,
    val timestamp: Long
)

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {

    val history: StateFlow<List<ScanRecord>> = repository.history.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var lastResult: ScanUiResult? = null
        private set

    fun setLastResult(result: ScanUiResult) {
        lastResult = result
    }

    fun saveCurrentResult() {
        val result = lastResult ?: return
        viewModelScope.launch {
            repository.insert(
                ScanRecord(
                    format = result.format,
                    value = result.value,
                    timestamp = result.timestamp
                )
            )
        }
    }

    fun deleteRecord(record: ScanRecord) {
        viewModelScope.launch { repository.delete(record) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.deleteAll() }
    }

    class Factory(private val repository: ScanRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScanViewModel(repository) as T
        }
    }
}
