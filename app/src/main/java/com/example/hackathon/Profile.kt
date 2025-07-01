package com.example.hackathon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfilePage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    Scaffold(
        bottomBar = {  FarmBottomNavigationBar(
            navController = navController,
            currentRoute = navController.currentBackStackEntry?.destination?.route)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Profile Page",
                style = MaterialTheme.typography.headlineMedium
            )
            // Add more herd content here
        }
    }
}