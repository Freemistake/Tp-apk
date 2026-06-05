package com.example.tp_apk.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun FakeErrorScreen(
    onTimeout: () -> Unit
)  {

    val erreurs = listOf(
        "Erreur réseau",
        "Session expirée",
        "Serveur indisponible",
        "Erreur d'authentification",
        "Erreur 403 - Accès refusé",
        "Impossible de joindre le serveur"
    )

    val erreur = remember {
        erreurs.random()
    }

    LaunchedEffect(Unit) {
        delay(5_000)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = erreur,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}