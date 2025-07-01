package com.example.hackathon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data class for a medical task
data class MedicalTask(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val status: String // "upcoming" or "history"
)

@Composable
fun TasksPage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    val tabTitles = listOf("Upcoming", "History")
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf<List<MedicalTask>>(emptyList()) }

    // Dummy data, replace with Firestore fetch logic
    LaunchedEffect(Unit) {
        isLoading = true
        tasks = listOf(
            MedicalTask("1", "Vet Visit", "Annual vaccination", "2024-06-10", "upcoming"),
            MedicalTask("2", "Deworming", "Routine deworming", "2024-05-20", "history"),
            MedicalTask("3", "Checkup", "General health check", "2024-06-15", "upcoming")
        )
        isLoading = false
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add new task */ },
                containerColor = Color(0xFF2E7D32),
                shape = RoundedCornerShape(50),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task", tint = Color.White)
            }
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
            Text(
                text = "🩺 Medical Tasks",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF2E7D32),
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == idx) Color(0xFF2E7D32) else Color.Gray,
                                fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF43A047))
                }
            } else {
                val filteredTasks = tasks.filter {
                    if (selectedTab == 0) it.status == "upcoming" else it.status == "history"
                }
                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedTab == 0) "No upcoming tasks." else "No history records.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredTasks) { task ->
                            MedicalTaskCard(task)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicalTaskCard(task: MedicalTask) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .clickable { /* TODO: Show details */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.MedicalServices,
                contentDescription = "Medical",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(task.description, color = Color.Gray, fontSize = 13.sp)
                Text(task.date, color = Color(0xFF2196F3), fontSize = 12.sp)
            }
        }
    }
}