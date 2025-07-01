package com.example.hackathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SignupScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    // State variables
    var mobileNumber by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var farmName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // Date picker state
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var selectedDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            selectedDate = dateFormat.format(calendar.time)
        }, year, month, day
    )
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = rememberAsyncImagePainter(
                "https://api.a0.dev/assets/image?text=Rural%20farm%20landscape%20with%20cattle%20grazing%20in%20fields&aspect=9:16&seed=123"
            ),
            contentDescription = "Farm Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xD9FFFFFF)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PashuSewa",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C8D23),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Your Farming Companion",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF6B4226),
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Text(
                        text = "Create Account",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4226),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person Icon",
                                tint = Color(0xFF5C8D23)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Mobile Number
                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        label = { Text("Mobile Number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone Icon",
                                tint = Color(0xFF5C8D23)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color(0xFF5C8D23)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Farm Name
                    OutlinedTextField(
                        value = farmName,
                        onValueChange = { farmName = it },
                        label = { Text("Farm Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Farm Icon",
                                tint = Color(0xFF5C8D23)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Date Picker
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { },
                        label = { Text("Farm Established Date") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar Icon",
                                tint = Color(0xFF5C8D23)
                            )
                        },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp)
                            .clickable { datePickerDialog.show() },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Text(
                                text = if (passwordVisible) "HIDE" else "SHOW",
                                modifier = Modifier.clickable {
                                    passwordVisible = !passwordVisible
                                },
                                color = Color(0xFF009688),
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Text(
                                text = if (passwordVisible) "HIDE" else "SHOW",
                                modifier = Modifier.clickable {
                                    passwordVisible = !passwordVisible
                                },
                                color = Color(0xFF009688),
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C8D23),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    Text(
                        text = "Use 6 or more characters with letters, numbers & symbols.",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error message
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    // Signup Button
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() &&
                                fullName.isNotBlank() && mobileNumber.isNotBlank()
                            ) {
                                if (password != confirmPassword) {
                                    errorMessage = "Passwords do not match"
                                    return@Button
                                }
                                isLoading = true
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val userId = auth.currentUser?.uid ?: ""
                                            val userMap = hashMapOf(
                                                "userId" to userId,
                                                "fullName" to fullName,
                                                "email" to email,
                                                "mobileNumber" to mobileNumber,
                                                "farmName" to farmName,
                                                "farmEstablishedDate" to selectedDate
                                            )
                                            firestore.collection("users")
                                                .document(userId)
                                                .set(userMap)
                                                .addOnSuccessListener {
                                                    isLoading = false
                                                    navController.navigate(RouteHomepage)
                                                }
                                                .addOnFailureListener { e ->
                                                    errorMessage =
                                                        "Failed to store user data: ${e.message}"
                                                    isLoading = false
                                                }
                                        } else {
                                            errorMessage = "Error: ${task.exception?.message}"
                                            isLoading = false
                                        }
                                    }
                            } else {
                                errorMessage = "Please fill in all fields"
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
                            Text(text = "CREATE ACCOUNT", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    // "Register as Vet" link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Are you a vet? ",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = {
                                navController.navigate(RouteVetSignup) {
                                    popUpTo(RouteSignupScreen) { inclusive = true }
                                }
                            }
                        ) {
                            Text(
                                text = "Register as Vet",
                                color = Color(0xFF5C8D23),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "By creating account you agree with our",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Terms & Conditions",
                        color = Color(0xFF009688),
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { /* Navigate to Terms & Conditions */ }
                    )
                }
            }
        }
    }
}
