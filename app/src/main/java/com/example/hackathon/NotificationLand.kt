package com.example.hackathon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    notification: CattleNotificationDetailed // pass full data object
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Case Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Owner Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Owner",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(notification.ownerName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(notification.location, fontSize = 14.sp, color = Color.Gray)
                    Text("📞 ${notification.contact}", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cattle Case Info
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cattle: ${notification.cattleName}", fontWeight = FontWeight.SemiBold)
                    Text("Symptoms: ${notification.symptoms}", color = Color.Gray, fontSize = 13.sp)
                    Text("Remarks: ${notification.remarks}", color = Color.Gray, fontSize = 13.sp)
                    Text("Status: ${notification.status}", fontWeight = FontWeight.Medium, color = Color(0xFFD32F2F))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Weekly Health Report",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF33691E),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            notification.weeklyData.forEach { data ->
                WeeklyHealthCard(data)
            }
        }
    }
}

@Composable
fun WeeklyHealthCard(data: WeeklyHealthData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${data.date}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            HealthMetricRow("Temperature", "${data.temperature} °C", Icons.Default.Thermostat)
            HealthMetricRow("Weight", "${data.weight} kg", Icons.Default.MonitorWeight)
            HealthMetricRow("Sleep", "${data.sleepHours} hrs", Icons.Default.Hotel)
            HealthMetricRow("Feed Intake", "${data.feedIntake} kg", Icons.Default.Restaurant)
            HealthMetricRow("Heart Rate", "${data.heartRate} bpm", Icons.Default.Favorite)
            HealthMetricRow("Milk Production", "${data.milkLitres} L", Icons.Default.LocalDrink)
        }
    }
}

@Composable
fun HealthMetricRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

// --- DATA CLASSES ---
data class CattleNotificationDetailed(
    val cattleName: String,
    val ownerName: String,
    val contact: String,
    val location: String,
    val symptoms: String,
    val remarks: String,
    val status: String,
    val weeklyData: List<WeeklyHealthData>
)

data class WeeklyHealthData(
    val date: String,
    val temperature: Float,
    val weight: Float,
    val sleepHours: Float,
    val feedIntake: Float,
    val heartRate: Int,
    val milkLitres: Float
)


