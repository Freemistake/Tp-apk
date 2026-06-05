package com.example.tp_apk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.tp_apk.data.DataStoreManager
import com.example.tp_apk.screens.*
import com.example.tp_apk.ui.theme.TpApkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStore = DataStoreManager(this)

        setContent {

            TpApkTheme {

                val navController = rememberNavController()
                val scope = rememberCoroutineScope()

                var startDestination by remember { mutableStateOf("loading") }

                LaunchedEffect(Unit) {

                    val lastLogin = dataStore.getLastLogin()

                    val TWO_DAYS = 2 * 60 * 1000L
                        //2 * 24 * 60 * 60 * 1000L
                    val now = System.currentTimeMillis()
                    startDestination =
                        if ((now - lastLogin) >= TWO_DAYS)
                            "login"
                        else
                            "home"
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("loading") {
                        Text("Loading...")
                    }
                    composable("login") {
                        LoginScreen {
                            scope.launch {
                                dataStore.saveLastLogin(System.currentTimeMillis())
                            }
                            navController.navigate("fake_error") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    composable("fake_error") {
                        FakeErrorScreen(
                            onTimeout = {
                                navController.navigate("home") {
                                    popUpTo("fake_error") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}