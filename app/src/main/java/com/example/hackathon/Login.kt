package com.example.hackathon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = LocalSessionManager.current,
    sessionState: SessionState
) {
    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Collect session state
    val sessionState by sessionViewModel.sessionState.collectAsState()

    // Effect to handle session state changes
    LaunchedEffect(sessionState) {
        if (sessionState.isAuthenticated) {
            if (sessionState.isVet) {
                navController.navigate(RouteVetProfile) {
                    popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
                }
            } else {
                navController.navigate(RouteHomepage) {
                    popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
                }
            }
        }
        sessionState.error?.let { error ->
            errorMessage = error
            sessionViewModel.clearError()
        }
    }

    // Colors
    val primaryGreen = Color(0xFF5C8D23)
    val primaryBrown = Color(0xFF6B4226)

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://images.unsplash.com/photo-1500382017468-9049fed747ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmFybXxlbnwwfHwwfHw%3D&w=1000&q=80")
                    .build()
            ),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Snackbar host for notifications
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card with login form
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .border(2.dp, primaryGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data("https://images.unsplash.com/photo-1533167649158-6d508895b680?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Y293fGVufDB8fDB8fA%3D%3D&w=1000&q=80")
                                    .build()
                            ),
                            contentDescription = "App Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // App name and subtitle
                    Text(
                        text = "PashuSewa",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Your Farming Companion",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = primaryBrown,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // Phone or Email input
                    OutlinedTextField(
                        value = phoneOrEmail,
                        onValueChange = { phoneOrEmail = it },
                        label = { Text("Mobile Number / Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = "Phone Icon",
                                tint = primaryBrown
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBrown,
                            unfocusedBorderColor = primaryBrown.copy(alpha = 0.7f),
                            focusedLabelColor = primaryBrown,
                            unfocusedLabelColor = primaryBrown.copy(alpha = 0.7f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Lock Icon",
                                tint = primaryBrown
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = primaryBrown
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBrown,
                            unfocusedBorderColor = primaryBrown.copy(alpha = 0.7f),
                            focusedLabelColor = primaryBrown,
                            unfocusedLabelColor = primaryBrown.copy(alpha = 0.7f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot Password
                    Text(
                        "FORGOT PASSWORD",
                        color = primaryGreen,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable(enabled = !isLoading) {
                                // Handle forgot password
                                scope.launch {
                                    snackbarHostState.showSnackbar("Forgot password feature coming soon!")
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Inside your LoginScreen composable, in the login button's onClick:
                  Button(
                      onClick = {
                          errorMessage = ""
                          isLoading = true
                          scope.launch {
                              try {
                                  val result = auth.signInWithEmailAndPassword(phoneOrEmail, password).await()
                                  val userId = result.user?.uid ?: throw Exception("User not found")
                                  val token = result.user?.getIdToken(false)?.await()?.token ?: throw Exception("Failed to get token")
                                  val firestore = FirebaseFirestore.getInstance()
                                  val vetDoc = firestore.collection("vets").document(userId).get().await()
                                  isLoading = false
                                  if (vetDoc.exists()) {
                                      // Vet login
                                      sessionViewModel.login(token, userId, true)
                                      navController.navigate(RouteVetProfile) {
                                          popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
                                      }
                                  } else {
                                      // Normal user login
                                      sessionViewModel.login(token, userId, false)
                                      navController.navigate(RouteHomepage) {
                                          popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
                                      }
                                  }
                              } catch (e: Exception) {
                                  isLoading = false
                                  errorMessage = e.message ?: "Login failed"
                              }
                          }
                      },
                      modifier = Modifier
                          .fillMaxWidth()
                          .height(48.dp)
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C8D23))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = "LOGIN", color = Color.White)
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign up text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Sign Up",
                            color = primaryGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                navController.navigate(RouteSignupScreen::class.qualifiedName ?: "")
                            }
                        )
                    }
                }
            }
        }
    }
}

private suspend fun performLogin(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    onError: (String) -> Unit
) {
    try {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        when {
            trimmedEmail.isEmpty() -> {
                onError("Email or mobile cannot be empty")
                return
            }
            trimmedPassword.isEmpty() -> {
                onError("Password cannot be empty")
                return
            }
        }
        val result = auth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword).await()
        result.user?.let { user ->
            val token = user.getIdToken(false).await().token ?: throw Exception("Failed to get token")
            sessionViewModel.login(
                token, user.uid,
                isVet = FirebaseFirestore.getInstance()
                    .collection("vets")
                    .document(user.uid)
                    .get().await().exists()
            )
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