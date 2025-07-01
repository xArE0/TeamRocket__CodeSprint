package com.example.hackathon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight


@Composable
fun DoctorNotificationScreen(
    navController: NavController,
    onNotificationClick: (CattleNotificationDetailed) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF9FBE7),
        bottomBar = { DoctorBottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Cattle Case Notifications",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF33691E),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            NotificationList(
                notifications = sampleNotifications,
                onNotificationClick = onNotificationClick
            )
        }
    }
}

@Composable
fun NotificationList(
    notifications: List<CattleNotification>,
    onNotificationClick: (CattleNotificationDetailed) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notifications) { notification ->
            NotificationCard(notification = notification) {
                // Convert simple notification to detailed one
                val detailed = CattleNotificationDetailed(
                    cattleName = notification.cattleName,
                    ownerName = notification.ownerName,
                    contact = "N/A", // Update this if you have real data
                    location = "Unknown", // Update accordingly
                    symptoms = notification.symptoms,
                    remarks = notification.description,
                    status = notification.status,
                    weeklyData = emptyList() // You can pass real data if needed
                )
                onNotificationClick(detailed)
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: CattleNotification,
    onClick: () -> Unit
) {
    val statusColor = when (notification.status) {
        "Urgent" -> Color(0xFFD32F2F)
        "Pending" -> Color(0xFFFFA000)
        "Reviewed" -> Color(0xFF388E3C)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Case: ${notification.cattleName}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = notification.status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(statusColor.copy(0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Owner: ${notification.ownerName}", fontSize = 13.sp, color = Color.DarkGray)
            Text("Reported Symptoms: ${notification.symptoms}", fontSize = 13.sp, color = Color.DarkGray)
            Text("Details: ${notification.description}", fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}

// Simplified Notification Data Class
data class CattleNotification(
    val cattleName: String,
    val ownerName: String,
    val symptoms: String,
    val description: String,
    val status: String
)

// Sample Notifications
val sampleNotifications = listOf(
    CattleNotification(
        cattleName = "Cow #123",
        ownerName = "Ramesh Thapa",
        symptoms = "Loss of appetite, fever",
        description = "Cattle hasn't eaten for 2 days. High temperature observed.",
        status = "Urgent"
    ),
    CattleNotification(
        cattleName = "Buffalo #87",
        ownerName = "Sita Tamang",
        symptoms = "Limping, swelling on leg",
        description = "Not able to walk properly, swelling seen on right hind leg.",
        status = "Pending"
    ),
    CattleNotification(
        cattleName = "Cow #215",
        ownerName = "Dipendra BK",
        symptoms = "No milk production",
        description = "Sudden drop in milk production since last week.",
        status = "Reviewed"
    )
)
