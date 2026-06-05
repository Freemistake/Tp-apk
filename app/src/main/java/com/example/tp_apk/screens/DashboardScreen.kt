package com.example.tp_apk.screens

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    val memoryInfo = ActivityManager.MemoryInfo()

    activityManager.getMemoryInfo(memoryInfo)

    val totalRam = memoryInfo.totalMem
    val availableRam = memoryInfo.availMem
    val ramUsage =
        ((totalRam - availableRam).toFloat() / totalRam.toFloat())

    val gb = 1024 * 1024 * 1024L

    val total = totalRam / gb
    val available = availableRam / gb

    val stat = StatFs(Environment.getDataDirectory().path)

    val totalBytes = stat.totalBytes
    val freeBytes = stat.availableBytes

    val usedBytes = totalBytes - freeBytes
    val totalStorage = totalBytes / gb
    val usedStorage = usedBytes / gb

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    val network = connectivityManager.activeNetwork
    val capabilities =
        connectivityManager.getNetworkCapabilities(network)

    val wifiConnected =
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ?: false

    LinearProgressIndicator(
        progress = { 0.65f }
    )
    LinearProgressIndicator(
        progress = { ramUsage }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Informations appareil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        MetricCard(
            "RAM",
            "${total - available} GB / $total GB"
        )
        MetricCard(
            "Stockage",
            "$usedStorage GB / $totalStorage GB"
        )
        MetricCard(
            "Wi-Fi",
            if (wifiConnected) "Connecté" else "Déconnecté"
        )
        MetricCard("Réseau mobile", "Activé")
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title)
            Text(value)
        }
    }
}