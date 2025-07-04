package com.example.hackathon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// --- Data class for heat detection info ---
data class HeatDetectionInfo(
    val heatStartTime: String,
    val status: String,
    val aiRemainingHours: Int
)

// --- Insemination Card Composable ---
@Composable
fun InseminationCard(
    cattle: Cattle,
    heatInfo: HeatDetectionInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with cattle info
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "🐮 ${cattle.tagNo} • ${cattle.breed}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = heatInfo.heatStartTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = { /* Handle action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Take Action")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = when (heatInfo.status) {
                            "Optimal" -> Color(0xFFE8F5E9)
                            "Warning" -> Color(0xFFFFF8E1)
                            else -> Color(0xFFFFEBEE)
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = heatInfo.status,
                        style = MaterialTheme.typography.titleSmall,
                        color = when (heatInfo.status) {
                            "Optimal" -> Color(0xFF2E7D32)
                            "Warning" -> Color(0xFFF57F17)
                            else -> Color(0xFFC62828)
                        },
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        text = "Heat Window",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "${heatInfo.aiRemainingHours} hrs remaining",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time progression bar
            HeatProgressBar(currentHours = heatInfo.aiRemainingHours)

            // Time labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("26h", style = MaterialTheme.typography.labelSmall)
                Text("23h", style = MaterialTheme.typography.labelSmall)
                Text("6h", style = MaterialTheme.typography.labelSmall)
                Text("1h", style = MaterialTheme.typography.labelSmall)
                Text("-3h", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

// --- Heat Progress Bar Composable ---
@Composable
fun HeatProgressBar(currentHours: Int) {
    val progress = when {
        currentHours > 23 -> 1.0f
        currentHours > 6 -> 0.75f
        currentHours > 1 -> 0.25f
        currentHours > -3 -> 0.1f
        else -> 0f
    }

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp)),
        color = when {
            currentHours > 23 -> Color(0xFF4CAF50)
            currentHours > 6 -> Color(0xFFFFC107)
            currentHours > 1 -> Color(0xFFF44336)
            else -> Color(0xFFB71C1C)
        },
        trackColor = Color.LightGray,
    )
}

// --- Coming Soon Card Composable ---
@Composable
fun ComingSoonCard(message: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Text(
                message,
                color = color,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

// --- Cattle Card Composable ---
@Composable
fun CattleCard(cattle: Cattle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🐮 Tag: ${cattle.tagNo} • ${cattle.type} (${cattle.gender})",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Breed: ${cattle.breed}  |  DOB: ${cattle.dob}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            if (cattle.weight.isNotBlank() || cattle.color.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = buildString {
                        if (cattle.weight.isNotBlank()) append("Weight: ${cattle.weight}kg  ")
                        if (cattle.color.isNotBlank()) append("Color: ${cattle.color}")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (cattle.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 Notes: ${cattle.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A4EB3)
                )
            }
        }
    }
}

// --- Main Herd Page Composable ---
@Composable
fun HerdPage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    val userId = sessionState.userId
    var cattleList by remember { mutableStateOf<List<Cattle>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val tabTitles = listOf("Herd", "Insemination", "Dairy")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) {
            isLoading = true
            cattleList = FirebaseDataClass().fetchCattleList(userId)
            isLoading = false
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddCattleScreen) { launchSingleTop = true } },
                    containerColor = Color(0xFF43A047),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Animal",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
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
                text = "🐄 Your Herd",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
            )

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
                0 -> {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF43A047))
                        }
                    } else if (cattleList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "No animals in your herd yet.",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(cattleList) { cattle ->
                                CattleCard(
                                    cattle = cattle,
                                    onClick = {
                                        navController.navigate(RouteDetailsPage(tag = cattle.tagNo))
                                    }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF43A047))
                        }
                    } else if (cattleList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "No insemination records yet.",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(cattleList) { cattle ->
                                InseminationCard(
                                    cattle = cattle,
                                    heatInfo = HeatDetectionInfo(
                                        heatStartTime = "2023-10-01 08:00",
                                        status = "Optimal",
                                        aiRemainingHours = 26
                                    ),
                                    onClick = {
                                        navController.navigate(RouteDetailsPage(tag = cattle.tagNo))
                                    }
                                )
                            }
                        }
                    }
                }
                2 -> ComingSoonCard("Dairy records coming soon.", Color(0xFF2196F3))
            }
        }
    }
}