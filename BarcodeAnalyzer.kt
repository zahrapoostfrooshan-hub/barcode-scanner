package com.example.barcodescanner.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * تحلیل‌گر فریم‌های دوربین برای تشخیص بارکد/QR Code به‌صورت آفلاین با ML Kit.
 * مدل ML Kit در همین کتابخانه باندل شده و نیازی به اینترنت ندارد.
 */
class BarcodeAnalyzer(
    private val onBarcodeDetected: (Barcode) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner: BarcodeScanner = BarcodeScanning.getClient()

    @Volatile
    private var isProcessing = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        isProcessing = true
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val barcode = barcodes.firstOrNull { !it.rawValue.isNullOrEmpty() }
                if (barcode != null) {
                    onBarcodeDetected(barcode)
                }
            }
            .addOnFailureListener {
                // در صورت بروز خطا، اسکن به‌طور خودکار در فریم بعدی ادامه پیدا می‌کند
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }
}
