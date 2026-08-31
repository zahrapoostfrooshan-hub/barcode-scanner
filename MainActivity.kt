package com.example.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.barcodescanner.ui.screens.HistoryScreen
import com.example.barcodescanner.ui.screens.HomeScreen
import com.example.barcodescanner.ui.screens.ResultScreen
import com.example.barcodescanner.ui.screens.ScanScreen
import com.example.barcodescanner.ui.theme.BarcodeScannerTheme
import com.example.barcodescanner.viewmodel.ScanUiResult
import com.example.barcodescanner.viewmodel.ScanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as BarcodeScannerApp

        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                BarcodeScannerTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val navController = rememberNavController()
                        val viewModel: ScanViewModel = viewModel(
                            factory = ScanViewModel.Factory(app.repository)
                        )

                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(
                                    onScanClick = { navController.navigate("scan") },
                                    onHistoryClick = { navController.navigate("history") }
                                )
                            }
                            composable("scan") {
                                ScanScreen(
                                    onBarcodeScanned = { format, value ->
                                        viewModel.setLastResult(
                                            ScanUiResult(
                                                format = format,
                                                value = value,
                                                timestamp = System.currentTimeMillis()
                                            )
                                        )
                                        navController.navigate("result") {
                                            popUpTo("scan") { inclusive = true }
                                        }
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("result") {
                                val result = viewModel.lastResult
                                if (result != null) {
                                    ResultScreen(
                                        result = result,
                                        onSave = { viewModel.saveCurrentResult() },
                                        onScanAgain = {
                                            navController.navigate("scan") {
                                                popUpTo("home")
                                            }
                                        },
                                        onBackHome = {
                                            navController.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        }
                                    )
                                }
                            }
                            composable("history") {
                                val historyList by viewModel.history.collectAsState()
                                HistoryScreen(
                                    records = historyList,
                                    onDelete = { viewModel.deleteRecord(it) },
                                    onClearAll = { viewModel.clearHistory() },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
