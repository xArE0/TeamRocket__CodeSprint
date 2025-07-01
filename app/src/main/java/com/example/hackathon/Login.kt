package com.example.hackathon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = LocalSessionManager.current
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Collect session state
    val sessionState by sessionViewModel.sessionState.collectAsState()

    // Effect to handle session state changes
    LaunchedEffect(sessionState) {
        if (sessionState.isAuthenticated) {
            navController.navigate(RouteHomepage::class.qualifiedName ?: "") {
                popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
            }
        }

        sessionState.error?.let { error ->
            errorMessage = error
            sessionViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Welcome Text
        Text(
            text = "Welcome",
            color = Color(0xFF4CAF50),
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Sign in to manage properties", color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Email / Mobile Number Field
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Mobile Number / Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password
        Text(
            "FORGOT PASSWORD",
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .align(Alignment.End)
                .clickable(enabled = !isLoading) {
                    // Handle forgot password
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    performLogin(
                        auth = auth,
                        email = email,
                        password = password,
                        context = context,
                        sessionViewModel = sessionViewModel,
                        navController = navController
                    ) { error ->
                        errorMessage = error
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("LOG IN")
            }
        }

        // Error message
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // New User Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(enabled = !isLoading) {
                // Navigate to registration
                navController.navigate(RouteSignupScreen)
            }
        ) {
            Text("New User? ")
            Text(
                "CREATE AN ACCOUNT",
                color = Color(0xFF4CAF50)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("OR", color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))
    }
}

private suspend fun performLogin(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: android.content.Context,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    onError: (String) -> Unit
) {
    try {
        // Trim input
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        // Validate input
        when {
            trimmedEmail.isEmpty() -> {
                onError("Email cannot be empty")
                return
            }
            trimmedPassword.isEmpty() -> {
                onError("Password cannot be empty")
                return
            }
        }

        // Attempt login
        val result = auth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword).await()

        result.user?.let { user ->
            // Generate token
            val token = user.getIdToken(false).await().token ?: throw Exception("Failed to get token")

            // Update session using SessionViewModel
            sessionViewModel.login(token, user.uid)
        } ?: throw Exception("Login failed: Unknown error")

    } catch (e: Exception) {
        val errorMessage = when (e) {
            is FirebaseAuthInvalidUserException -> "No account exists with this email"
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
            else -> "Error: ${e.message}"
        }
        onError(errorMessage)
        sessionViewModel.clearError()
    }
}