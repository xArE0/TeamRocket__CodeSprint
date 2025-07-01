package com.example.hackathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TasksPage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    val tabTitles = listOf("Doctor", "Help", "Vaccination")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        topBar = {
            Text(
                text = "🐄 Farm Care",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
            )
        },
        bottomBar = {
            FarmBottomNavigationBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFF2E7D32),
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.weight(1f),
                        text = {
                            Text(
                                title,
                                color = if (selectedTabIndex == index) Color(0xFF2E7D32) else Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> DoctorTabContent()
                1 -> HelpTabContent()
                2 -> VaccinationTabContent()
            }
        }
    }
}

data class DoctorProfile(
    val name: String,
    val location: String,
    val description: String
)

@Composable
fun DoctorTabContent() {
    val doctors = listOf(
        DoctorProfile(
            "Dr. Alex",
            "Wandegeya",
            "Can treat all types of diseases affecting pigs ..."
        ),
        DoctorProfile(
            "Dr. Atim Nancy",
            "Jinja",
            "Focuses on preventive medicine as well as surgical intervention, particularly for spaying and neutering pigs ..."
        ),
        DoctorProfile(
            "Dr. Namayengo Doreen",
            "Masaka",
            "Responsible for overseeing all daily aspects of swine breeding, including maintaining breeding targets, dai..."
        ),
        DoctorProfile(
            "Dr. Happy Joshua",
            "Kiryandongo",
            "My role as piggery veterinarians play in the day-to-day operations of a modern pig farm is vital, and their res..."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(doctors) { doctor ->
            DoctorProfileCard(doctor = doctor, onClick = { /* Handle profile click */ })
        }
    }
}

@Composable
fun DoctorProfileCard(doctor: DoctorProfile, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6FFFB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Avatar (replace with your own image if available)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                // If you have a doctor avatar drawable, use Image(painterResource(id = R.drawable.doctor_avatar), ...)
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Doctor Avatar",
                    tint = Color(0xFF7A4EB3),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Info and actions
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = doctor.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = doctor.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(onClick = { /* Help */ }) {
                        Icon(
                            imageVector = Icons.Default.Help,
                            contentDescription = "Help",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = { /* Location */ }) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location",
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = { /* Call */ }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color(0xFF388E3C),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpTabContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Help resources coming soon!", color = Color(0xFF388E3C))
        }
    }
}

@Composable
fun VaccinationTabContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Vaccination records coming soon!", color = Color(0xFFFBC02D))
        }
    }
}