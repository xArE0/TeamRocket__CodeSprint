package com.example.hackathon

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_data")

// Session state data class
data class SessionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val userId: String? = null,
    val userToken: String? = null,
    val isVet: Boolean = false // Add this
)

// Session Manager object
object SessionManager {
    private val USER_ID = stringPreferencesKey("user_id")
    private val TOKEN = stringPreferencesKey("token")

    suspend fun saveUserData(context: Context, userId: String, token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[TOKEN] = token
        }
    }

    fun getUserId(context: Context): Flow<String?> =
        context.dataStore.data.map { it[USER_ID] }

    fun getToken(context: Context): Flow<String?> =
        context.dataStore.data.map { it[TOKEN] }

    fun isAuthenticated(context: Context): Flow<Boolean> =
        getToken(context).map { it != null }

    suspend fun clearSession(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}

// CompositionLocal for session management
val LocalSessionManager = staticCompositionLocalOf<SessionViewModel> {
    error("No SessionViewModel provided")
}

// Session ViewModel
class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            try {
                _sessionState.update { it.copy(isLoading = true) }
                combine(
                    SessionManager.getToken(getApplication()),
                    SessionManager.getUserId(getApplication())
                ) { token, userId ->
                    SessionState(
                        isLoading = false,
                        isAuthenticated = token != null,
                        userId = userId,
                        userToken = token
                    )
                }.catch { error ->
                    Log.e("SessionViewModel", "Error loading session", error)
                    _sessionState.update { it.copy(isLoading = false, error = error.message) }
                }.collect { state ->
                    _sessionState.value = state
                }
            } catch (e: Exception) {
                Log.e("SessionViewModel", "Unexpected error loading session", e)
                _sessionState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    suspend fun login(token: String, userId: String, isVet: Boolean) {
        try {
            _sessionState.update { it.copy(isLoading = true) }
            SessionManager.saveUserData(getApplication(), userId, token)
            _sessionState.update {
                it.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userId = userId,
                    userToken = token,
                    isVet = isVet
                )
            }
        } catch (e: Exception) {
            Log.e("SessionViewModel", "Login error", e)
            _sessionState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    suspend fun logout() {
        try {
            _sessionState.update { it.copy(isLoading = true) }
            SessionManager.clearSession(getApplication())
            _sessionState.update {
                it.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    userId = null,
                    userToken = null
                )
            }
        } catch (e: Exception) {
            Log.e("SessionViewModel", "Logout error", e)
            _sessionState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    fun clearError() {
        _sessionState.update {
            if (it.error != null) it.copy(error = null) else it
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]?.let {
                    SessionViewModel(it)
                } ?: throw IllegalStateException("Application is null")
            }
        }
    }
}
