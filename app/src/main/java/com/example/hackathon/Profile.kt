package com.example.hackathon

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetProfileScreen(navController: NavController) {
    // State for edit mode
    var isEditMode by remember { mutableStateOf(false) }

    // States for user profile data
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var fullName by remember { mutableStateOf("Dr. Rahul Sharma") }
    var phoneNumber by remember { mutableStateOf("9876543210") }
    var email by remember { mutableStateOf("rahul.sharma@vetmail.com") }
    var vetRegNumber by remember { mutableStateOf("VCI-MH-2345") }
    var specialization by remember { mutableStateOf("Small Animal Medicine") }
    var clinicName by remember { mutableStateOf("PetCare Veterinary Clinic") }
    var clinicAddress by remember { mutableStateOf("123, Green Avenue, Andheri East, Mumbai - 400069") }

    // Temporary states for edit mode
    var tempPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var tempEmail by remember { mutableStateOf(email) }
    var tempSpecialization by remember { mutableStateOf(specialization) }
    var tempClinicName by remember { mutableStateOf(clinicName) }
    var tempClinicAddress by remember { mutableStateOf(clinicAddress) }

    // Error states
    var phoneNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var clinicNameError by remember { mutableStateOf("") }
    var clinicAddressError by remember { mutableStateOf("") }

    // Context for Toast
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
        }
    }

    // Specialization dropdown state
    var isSpecializationExpanded by remember { mutableStateOf(false) }

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

    // Validation function
    fun validateForm(): Boolean {
        var isValid = true

        // Phone Number validation
        if (tempPhoneNumber.trim().isEmpty()) {
            phoneNumberError = "Phone number is required"
            isValid = false
        } else if (!tempPhoneNumber.matches(Regex("^\\d{10}$"))) {
            phoneNumberError = "Enter a valid 10-digit phone number"
            isValid = false
        } else {
            phoneNumberError = ""
        }

        // Email validation
        if (tempEmail.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
            emailError = "Enter a valid email address"
            isValid = false
        } else {
            emailError = ""
        }

        // Clinic Name validation
        if (tempClinicName.trim().isEmpty()) {
            clinicNameError = "Clinic name is required"
            isValid = false
        } else {
            clinicNameError = ""
        }

        // Clinic Address validation
        if (tempClinicAddress.trim().isEmpty()) {
            clinicAddressError = "Clinic address is required"
            isValid = false
        } else {
            clinicAddressError = ""
        }

        return isValid
    }

    // Function to handle save action
    fun handleSave() {
        if (validateForm()) {
            // Update the actual values with temporary values
            phoneNumber = tempPhoneNumber
            email = tempEmail
            specialization = tempSpecialization
            clinicName = tempClinicName
            clinicAddress = tempClinicAddress

            // Exit edit mode
            isEditMode = false

            // Show success message
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to handle cancel action
    fun handleCancel() {
        // Reset temporary values to actual values
        tempPhoneNumber = phoneNumber
        tempEmail = email
        tempSpecialization = specialization
        tempClinicName = clinicName
        tempClinicAddress = clinicAddress

        // Clear error states
        phoneNumberError = ""
        emailError = ""
        clinicNameError = ""
        clinicAddressError = ""

        // Exit edit mode
        isEditMode = false
    }

    // Function to enter edit mode
    fun enterEditMode() {
        // Initialize temporary values with current values
        tempPhoneNumber = phoneNumber
        tempEmail = email
        tempSpecialization = specialization
        tempClinicName = clinicName
        tempClinicAddress = clinicAddress

        // Enter edit mode
        isEditMode = true
    }

    // Main UI
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
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "PashuSewa",
                        color = PashuSewaGreen,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PashuSewaGreen
                        )
                    }
                },
                actions = {
                    if (!isEditMode) {
                        IconButton(onClick = { enterEditMode() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = PashuSewaGreen
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Profile Content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Header with Edit Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vet Doctor Profile",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = PashuSewaGreen,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (isEditMode) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge(
                                containerColor = PashuSewaGreen,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Text(
                                    text = "Editing",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .border(2.dp, PashuSewaGreen, CircleShape)
                            .clickable(enabled = isEditMode) {
                                if (isEditMode) {
                                    imagePickerLauncher.launch("image/*")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(profileImageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        if (isEditMode) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Picture",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    if (isEditMode) {
                        Text(
                            text = "Tap to change profile picture",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Full Name (Read-only)
                    ProfileField(
                        label = "Full Name",
                        value = fullName,
                        icon = Icons.Outlined.Person,
                        isEditable = false,
                        isEditMode = isEditMode
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Phone Number
                    if (isEditMode) {
                        OutlinedTextField(
                            value = tempPhoneNumber,
                            onValueChange = {
                                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                    tempPhoneNumber = it
                                    if (phoneNumberError.isNotEmpty()) phoneNumberError = ""
                                }
                            },
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone") },
                            isError = phoneNumberError.isNotEmpty(),
                            supportingText = { if (phoneNumberError.isNotEmpty()) Text(phoneNumberError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                    } else {
                        ProfileField(
                            label = "Phone Number",
                            value = phoneNumber,
                            icon = Icons.Outlined.Phone,
                            isEditable = true,
                            isEditMode = isEditMode,
                            onEditClick = { enterEditMode() }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Email Address
                    if (isEditMode) {
                        OutlinedTextField(
                            value = tempEmail,
                            onValueChange = {
                                tempEmail = it
                                if (emailError.isNotEmpty()) emailError = ""
                            },
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                            isError = emailError.isNotEmpty(),
                            supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )
                    } else {
                        ProfileField(
                            label = "Email Address",
                            value = email,
                            icon = Icons.Outlined.Email,
                            isEditable = true,
                            isEditMode = isEditMode,
                            onEditClick = { enterEditMode() }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Vet Registration Number (Read-only)
                    ProfileField(
                        label = "Vet Registration Number",
                        value = vetRegNumber,
                        icon = Icons.Outlined.Shield,
                        isEditable = false,
                        isEditMode = isEditMode
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Specialization
                    if (isEditMode) {
                        ExposedDropdownMenuBox(
                            expanded = isSpecializationExpanded,
                            onExpandedChange = { isSpecializationExpanded = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            OutlinedTextField(
                                value = tempSpecialization,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Specialization") },
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
                                            tempSpecialization = option
                                            isSpecializationExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        ProfileField(
                            label = "Specialization",
                            value = specialization,
                            icon = Icons.Outlined.MedicalServices,
                            isEditable = true,
                            isEditMode = isEditMode,
                            onEditClick = { enterEditMode() }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Clinic Name
                    if (isEditMode) {
                        OutlinedTextField(
                            value = tempClinicName,
                            onValueChange = {
                                tempClinicName = it
                                if (clinicNameError.isNotEmpty()) clinicNameError = ""
                            },
                            label = { Text("Clinic Name") },
                            leadingIcon = { Icon(Icons.Outlined.Business, contentDescription = "Business") },
                            isError = clinicNameError.isNotEmpty(),
                            supportingText = { if (clinicNameError.isNotEmpty()) Text(clinicNameError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    } else {
                        ProfileField(
                            label = "Clinic Name",
                            value = clinicName,
                            icon = Icons.Outlined.Business,
                            isEditable = true,
                            isEditMode = isEditMode,
                            onEditClick = { enterEditMode() }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Clinic Address
                    if (isEditMode) {
                        OutlinedTextField(
                            value = tempClinicAddress,
                            onValueChange = {
                                tempClinicAddress = it
                                if (clinicAddressError.isNotEmpty()) clinicAddressError = ""
                            },
                            label = { Text("Clinic Address") },
                            leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = "Location") },
                            isError = clinicAddressError.isNotEmpty(),
                            supportingText = { if (clinicAddressError.isNotEmpty()) Text(clinicAddressError) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(100.dp),
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )
                    } else {
                        ProfileField(
                            label = "Clinic Address",
                            value = clinicAddress,
                            icon = Icons.Outlined.LocationOn,
                            isEditable = true,
                            isEditMode = isEditMode,
                            isMultiline = true,
                            onEditClick = { enterEditMode() }
                        )
                    }

                    // Action Buttons
                    if (isEditMode) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { handleCancel() },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                border = BorderStroke(1.dp, PashuSewaGreen)
                            ) {
                                Text(
                                    text = "CANCEL",
                                    color = PashuSewaGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { handleSave() },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PashuSewaGreen)
                            ) {
                                Text(
                                    text = "SAVE",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Edit button for non-edit mode
                        Button(
                            onClick = { enterEditMode() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PashuSewaGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "EDIT PROFILE",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isEditable: Boolean,
    isEditMode: Boolean,
    isMultiline: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = isEditable && !isEditMode) {
                if (isEditable && !isEditMode) {
                    onEditClick()
                }
            },
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = PashuSewaGreen,
            modifier = Modifier.padding(end = 16.dp, top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = if (isMultiline) FontWeight.Normal else FontWeight.Medium,
                color = Color.Black,
                modifier = if (isMultiline) Modifier.padding(top = 4.dp) else Modifier
            )
        }

        if (isEditable && !isEditMode) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = PashuSewaGreen,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}