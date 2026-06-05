package com.example.tp_apk.data


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val LAST_LOGIN = longPreferencesKey("last_login")
    }

    suspend fun saveUser(username: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME] = username
            prefs[PASSWORD] = password
        }
    }
    suspend fun saveLastLogin(time: Long) {
        context.dataStore.edit {
            it[LAST_LOGIN] = time
        }
    }
    suspend fun getUser(): Pair<String, String> {
        val prefs = context.dataStore.data.first()
        return Pair(
            prefs[USERNAME] ?: "",
            prefs[PASSWORD] ?: ""
        )
    }

    suspend fun getLastLogin(): Long {
        val prefs = context.dataStore.data.first()
        return prefs[LAST_LOGIN] ?: 0L
    }
}