package com.example.hackathon

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val PashuSewaGreen = Color(0xFF5C8D23)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetSignupScreen(navController: NavController) {
    // Form state
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var vetRegNumber by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var clinicName by remember { mutableStateOf("") }
    var clinicAddress by remember { mutableStateOf("") }

    // Error states
    var fullNameError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var vetRegNumberError by remember { mutableStateOf("") }
    var clinicNameError by remember { mutableStateOf("") }
    var clinicAddressError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Dropdown state
    var isSpecializationExpanded by remember { mutableStateOf(false) }

    // Password visibility
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Specialization options
    val specializationOptions = listOf(
        "Small Animal Medicine",
        "Large Animal Medicine",
        "Surgery",
        "Dermatology",
        "Cardiology",
        "Neurology",
        "Oncology",
        "Ophthalmology",
        "Dentistry",
        "Emergency & Critical Care",
        "Other"
    )

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    fun validateForm(): Boolean {
        var isValid = true

        if (fullName.trim().isEmpty()) {
            fullNameError = "Full name is required"
            isValid = false
        } else fullNameError = ""

        if (phoneNumber.trim().isEmpty()) {
            phoneNumberError = "Phone number is required"
            isValid = false
        } else if (!phoneNumber.matches(Regex("^\\d{10}$"))) {
            phoneNumberError = "Enter a valid 10-digit phone number"
            isValid = false
        } else phoneNumberError = ""

        if (email.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Enter a valid email address"
            isValid = false
        } else emailError = ""

        if (password.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        } else passwordError = ""

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        } else confirmPasswordError = ""

        if (vetRegNumber.trim().isEmpty()) {
            vetRegNumberError = "Registration number is required"
            isValid = false
        } else vetRegNumberError = ""

        if (clinicName.trim().isEmpty()) {
            clinicNameError = "Clinic name is required"
            isValid = false
        } else clinicNameError = ""

        if (clinicAddress.trim().isEmpty()) {
            clinicAddressError = "Clinic address is required"
            isValid = false
        } else clinicAddressError = ""

        return isValid
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PashuSewa",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PashuSewaGreen,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .alpha(0.85f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Vet Doctor Registration",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PashuSewaGreen,
                        modifier = Modifier.padding(bottom = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            if (fullNameError.isNotEmpty()) fullNameError = ""
                        },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Person") },
                        isError = fullNameError.isNotEmpty(),
                        supportingText = { if (fullNameError.isNotEmpty()) Text(fullNameError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                phoneNumber = it
                                if (phoneNumberError.isNotEmpty()) phoneNumberError = ""
                            }
                        },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone") },
                        isError = phoneNumberError.isNotEmpty(),
                        supportingText = { if (phoneNumberError.isNotEmpty()) Text(phoneNumberError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Email Address
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailError.isNotEmpty()) emailError = ""
                        },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                        isError = emailError.isNotEmpty(),
                        supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError.isNotEmpty()) passwordError = ""
                            if (confirmPassword.isNotEmpty() && confirmPasswordError.isNotEmpty()) {
                                confirmPasswordError = if (it == confirmPassword) "" else "Passwords do not match"
                            }
                        },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Lock") },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = if (it == password) "" else "Passwords do not match"
                        },
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Lock") },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = confirmPasswordError.isNotEmpty(),
                        supportingText = { if (confirmPasswordError.isNotEmpty()) Text(confirmPasswordError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )

                    // Vet Registration Number
                    OutlinedTextField(
                        value = vetRegNumber,
                        onValueChange = {
                            vetRegNumber = it
                            if (vetRegNumberError.isNotEmpty()) vetRegNumberError = ""
                        },
                        label = { Text("Vet Registration Number") },
                        leadingIcon = { Icon(Icons.Outlined.Shield, contentDescription = "Shield") },
                        isError = vetRegNumberError.isNotEmpty(),
                        supportingText = { if (vetRegNumberError.isNotEmpty()) Text(vetRegNumberError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    // Specialization (Dropdown)
                    ExposedDropdownMenuBox(
                        expanded = isSpecializationExpanded,
                        onExpandedChange = { isSpecializationExpanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = specialization,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Specialization (Optional)") },
                            leadingIcon = { Icon(Icons.Outlined.MedicalServices, contentDescription = "Medical") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSpecializationExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = isSpecializationExpanded,
                            onDismissRequest = { isSpecializationExpanded = false }
                        ) {
                            specializationOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        specialization = option
                                        isSpecializationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Clinic Name
                    OutlinedTextField(
                        value = clinicName,
                        onValueChange = {
                            clinicName = it
                            if (clinicNameError.isNotEmpty()) clinicNameError = ""
                        },
                        label = { Text("Clinic Name") },
                        leadingIcon = { Icon(Icons.Outlined.Business, contentDescription = "Business") },
                        isError = clinicNameError.isNotEmpty(),
                        supportingText = { if (clinicNameError.isNotEmpty()) Text(clinicNameError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    // Clinic Address
                    OutlinedTextField(
                        value = clinicAddress,
                        onValueChange = {
                            clinicAddress = it
                            if (clinicAddressError.isNotEmpty()) clinicAddressError = ""
                        },
                        label = { Text("Clinic Address") },
                        leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = "Location") },
                        isError = clinicAddressError.isNotEmpty(),
                        supportingText = { if (clinicAddressError.isNotEmpty()) Text(clinicAddressError) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .height(100.dp),
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )

                    // General error
                    if (generalError.isNotEmpty()) {
                        Text(
                            text = generalError,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            if (validateForm()) {
                                isLoading = true
                                generalError = ""
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener { result ->
                                        val vetId = result.user?.uid ?: ""
                                        val vetData = hashMapOf(
                                            "fullName" to fullName,
                                            "phoneNumber" to phoneNumber,
                                            "email" to email,
                                            "vetRegNumber" to vetRegNumber,
                                            "specialization" to specialization,
                                            "clinicName" to clinicName,
                                            "clinicAddress" to clinicAddress
                                        )
                                        firestore.collection("vets").document(vetId).set(vetData)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                Toast.makeText(context, "Vet registration successful!", Toast.LENGTH_SHORT).show()
                                                navController.popBackStack()
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                generalError = "Failed to save vet data: ${e.message}"
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        generalError = "Registration failed: ${e.message}"
                                    }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PashuSewaGreen),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "REGISTER",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }

                    // Login Link
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color.Gray
                        )
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text(
                                text = "Login",
                                color = PashuSewaGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}