package com.example.barcodescanner.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.barcodescanner.viewmodel.ScanUiResult

@Composable
fun ResultScreen(
    result: ScanUiResult,
    onSave: () -> Unit,
    onScanAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    val context = LocalContext.current
    var isSaved by remember { mutableStateOf(false) }
    val isUrl = result.value.startsWith("http://") || result.value.startsWith("https://")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("اسکن با موفقیت انجام شد", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("نوع بارکد", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(result.format, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(16.dp))

                Text("مقدار", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(result.value, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { copyToClipboard(context, result.value) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("کپی")
            }
            Button(
                onClick = {
                    onSave()
                    isSaved = true
                    Toast.makeText(context, "در تاریخچه ذخیره شد", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                enabled = !isSaved
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isSaved) "ذخیره شد" else "ذخیره")
            }
        }

        if (isUrl) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.value))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.OpenInBrowser, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("باز کردن در مرورگر")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onScanAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("اسکن دوباره")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBackHome) {
            Text("بازگشت به صفحه اصلی")
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("scan_result", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show()
}
