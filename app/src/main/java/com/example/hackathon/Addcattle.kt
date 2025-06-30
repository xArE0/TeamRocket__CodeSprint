package com.example.hackathon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAnimalScreen(navController: NavController,
                       sessionViewModel: SessionViewModel,) {


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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Add New Animal",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E7D32))
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                // Allow only digits
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
            onClick = { /* handle submit */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A4EB3))
        ) {
            Text("Submit", color = Color.White)
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
            modifier = Modifier.fillMaxWidth()
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
