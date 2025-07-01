package com.example.hackathon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/*-------------------------------------------Main UI Function--------------------------------------------------*/
@Composable
fun Homepage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    val backgroundPainter = painterResource(id = R.drawable.farm__background)
    val userId = sessionState.userId
    var userName by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Fetch user name from Firestore
    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) {
            val user = FirebaseDataClass().fetchUserData(userId)
            userName = user?.fullName ?: "Farmer"
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddCattleScreen) {
                    launchSingleTop = true
                }},
                containerColor = Color(0xFF2E7D32), // Forest green
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Cattle",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {  FarmBottomNavigationBar(
            navController = navController,
            currentRoute = navController.currentBackStackEntry?.destination?.route)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = backgroundPainter,
                    contentScale = ContentScale.Crop
                )
                .padding(innerPadding)
        ) {
            FarmHeader(userName)
            QuickStatsSection()
            FarmActionsSection()
            RecentActivitySection()
        }
    }
}

@Composable
fun FarmHeader(userName: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            "🌾 Good Morning, ${userName ?: "Farmer"}!",
            color = Color(0xFF1B5E20),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Manage your herd with ease",
            color = Color(0xFF2E7D32),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun QuickStatsSection() {
    val stats = listOf(
        StatsCard("Total Cattle", "0", "🐄", Color(0xFF4CAF50)),
        StatsCard("Milk Today", "0 L", "🥛", Color(0xFF2196F3)),
        StatsCard("Pregnant", "0", "🤱", Color(0xFFFF9800)),
        StatsCard("Health Alerts", "0", "⚕️", Color(0xFFF44336))
    )

    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats) { stat ->
            StatsCardItem(stat)
        }
    }
}

@Composable
fun StatsCardItem(stats: StatsCard) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    stats.emoji,
                    fontSize = 20.sp
                )
            }
            Column {
                Text(
                    stats.value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = stats.color
                )
                Text(
                    stats.title,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FarmActionsSection() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "Farm Management",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val actions = listOf(
            FarmAction(Icons.Filled.Pets, "Herd Records", "Manage cattle profiles", Color(0xFF4CAF50)),
            FarmAction(Icons.Filled.LocalDrink, "Milk Production", "Track daily milk yield", Color(0xFF2196F3)),
            FarmAction(Icons.Filled.Favorite, "Breeding", "Breeding management", Color(0xFFE91E63)),
            FarmAction(Icons.Filled.MedicalServices, "Health", "Health monitoring", Color(0xFFF44336)),
            FarmAction(Icons.Filled.Agriculture, "Feed Management", "Feed schedules & costs", Color(0xFF8BC34A)),
            FarmAction(Icons.Filled.Assessment, "Reports", "Analytics & insights", Color(0xFF9C27B0))
        )

        for (i in actions.indices step 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmActionCard(
                    action = actions[i],
                    modifier = Modifier.weight(1f)
                )

                if (i + 1 < actions.size) {
                    FarmActionCard(
                        action = actions[i + 1],
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun FarmActionCard(
    action: FarmAction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { /* Handle action click */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        action.color.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    action.icon,
                    contentDescription = action.title,
                    tint = action.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    action.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    action.description,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun RecentActivitySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Recent Activity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "No recent activity",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    "Add your first cattle to get started!",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/*-------------------------------------------Bottom Bar (Same For Every Page)--------------------------------------------------*/
@Composable
fun FarmBottomNavigationBar(navController: NavController, currentRoute: String?) {
    val selectedColor = Color(0xFF2E7D32)
    val unselectedColor = Color(0xFF8E8E8E)
    val backgroundColor = Color.White.copy(alpha = 0.85f)

    val navItems = listOf(
        Triple(Icons.Filled.Home, "Home", RouteHomepage::class.qualifiedName!!),
        Triple(Icons.Filled.Pets, "Herd", RouteHerdPage::class.qualifiedName!!),
        Triple(Icons.AutoMirrored.Filled.Assignment, "Tasks", RouteTasksPage::class.qualifiedName!!),
        Triple(Icons.Outlined.Person, "Profile", RouteProfilePage::class.qualifiedName!!)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { (icon, label, route) ->
                val selected = currentRoute == route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        if (!selected) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = if (selected) selectedColor else unselectedColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        label,
                        color = if (selected) selectedColor else unselectedColor,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// Data classes
data class StatsCard(
    val title: String,
    val value: String,
    val emoji: String,
    val color: Color
)

data class FarmAction(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: Color
)