package com.example.hackathon

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAnimalScreen(navController: NavController, sessionViewModel: SessionViewModel) {
    val context = LocalContext.current

    var statusMessage by remember { mutableStateOf<String?>(null) }
    var statusColor by remember { mutableStateOf(Color.Unspecified) }

    val animalTypes = listOf("Calf", "Cow", "Bull")
    val genders = listOf("Male", "Female")
    val breeds = listOf("Lime", "Jersey", "Holstein", "Siri", "Brown Swiss", "Chauri")
    val purposes = listOf("Cull", "Dairy", "Fattening", "Suckler")
    var tagNo by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var tbDate by remember { mutableStateOf("") }
    var brDate by remember { mutableStateOf("") }
    var selectedPurpose by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val sessionState by sessionViewModel.sessionState.collectAsState()
    val userId = sessionState.userId
    val scope = rememberCoroutineScope()
    var isSubmitting by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    if (statusMessage != null) {
        Text(
            text = statusMessage!!,
            color = statusColor,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp)
        ) {
            // Top App Bar with Back Button
            TopAppBar(
                title = {
                    Text(
                        "Add New Animal",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = tagNo,
                        onValueChange = { tagNo = it },
                        label = { Text("Tag No / ID *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownSelector("Animal Type *", animalTypes, selectedType) { selectedType = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownSelector("Gender *", genders, selectedGender) { selectedGender = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownSelector("Breed *", breeds, selectedBreed) { selectedBreed = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = dob,
                        onValueChange = { dob = it },
                        label = { Text("Date of Birth *") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tbDate,
                        onValueChange = { tbDate = it },
                        label = { Text("Next TB Test Date (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select TB Test Date")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = brDate,
                        onValueChange = { brDate = it },
                        label = { Text("Next BR Due Date (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select BR Due Date")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DropdownSelector("Purpose *", purposes, selectedPurpose) { selectedPurpose = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = weight,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                weight = input
                            }
                        },
                        label = { Text("Weight (kg) - optional") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = color,
                        onValueChange = { color = it },
                        label = { Text("Color - optional") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes - optional") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3D8F6))
                    ) {
                        Text("Add Photo (Optional)", color = Color.White)
                    }

                    imageUri?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "Selected Cow Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(vertical = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Button(
                        onClick = {
                            if (!isSubmitting && tagNo.isNotBlank() && selectedType.isNotBlank() && selectedGender.isNotBlank() && selectedBreed.isNotBlank() && dob.isNotBlank() && selectedPurpose.isNotBlank() && userId != null) {
                                isSubmitting = true
                                statusMessage = "Submitting, please wait..."
                                statusColor = Color.Gray
                                scope.launch {
                                    var imageUrl: String? = null
                                    var error: String? = null
                                    if (imageUri != null) {
                                        imageUrl = FirebaseDataClass().uploadImageToImgbb(context, imageUri!!)
                                        if (imageUrl == null) {
                                            error = "Image upload failed. Please try again."
                                        }
                                    }
                                    if (error == null) {
                                        try {
                                            val cattle = Cattle(
                                                tagNo = tagNo,
                                                type = selectedType,
                                                gender = selectedGender,
                                                breed = selectedBreed,
                                                dob = dob,
                                                tbDate = tbDate,
                                                brDate = brDate,
                                                purpose = selectedPurpose,
                                                weight = weight,
                                                color = color,
                                                notes = notes,
                                                imageUrl = imageUrl
                                            )
                                            FirebaseDataClass().addCattle(userId, cattle)
                                            statusMessage = "Success! Animal added."
                                            statusColor = Color(0xFF2E7D32)
                                            kotlinx.coroutines.delay(1200)
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            statusMessage = "Failed to add animal: ${e.localizedMessage}"
                                            statusColor = Color.Red
                                        }
                                    } else {
                                        statusMessage = error
                                        statusColor = Color.Red
                                    }
                                    isSubmitting = false
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text(
                            if (isSubmitting) "Submitting..." else "Submit",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}