package com.example.hackathon

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun DoctorProfileScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    var isAvailable by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = Color(0xFFF1F8E9),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAvailable = !isAvailable },
                containerColor = if (isAvailable) Color(0xFF388E3C) else Color.Gray,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = if (isAvailable) Icons.Default.EventAvailable else Icons.Default.EventBusy,
                    contentDescription = "Toggle Availability",
                    tint = Color.White
                )
            }
        },
        bottomBar = { DoctorBottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            DoctorHeader(imageUrl = null) // Replace null with valid URL if available
            Spacer(modifier = Modifier.height(16.dp))
            DoctorInfoSection(isAvailable)
        }
    }
}

@Composable
fun DoctorHeader(imageUrl: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(imageUrl ?: R.drawable.default_profile) // fallback image
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "Doctor Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Dr. Nabin Thapa", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text("Vet Doctor", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DoctorInfoSection(isAvailable: Boolean) {
    val infoItems = listOf(
        DoctorInfoCard("Contact", "+977 9812345678", Icons.Default.Phone, Color(0xFFFB8C00)),
        DoctorInfoCard("Email Address", "aayush.sharma@hospital.com", Icons.Default.Email, Color(0xFF8E24AA)),
        DoctorInfoCard("VAT Registration No.", "VAT123456789", Icons.Default.ConfirmationNumber, Color(0xFF5E35B1)),
        DoctorInfoCard("Specialization", "Cattle Diseases, Surgery", Icons.Default.MedicalServices, Color(0xFF1E88E5)),
        DoctorInfoCard("Clinic Name", "Kathmandu Animal Health Center", Icons.Default.LocalHospital, Color(0xFFD81B60)),
        DoctorInfoCard("Clinic Address", "KMC, Sinamangal, Kathmandu", Icons.Default.LocationOn, Color(0xFF0097A7)),
        DoctorInfoCard("Availability", if (isAvailable) "Available" else "Unavailable", Icons.Default.AccessTime, if (isAvailable) Color(0xFF388E3C) else Color.Gray)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        infoItems.forEach {
            DoctorInfoCardItem(it)
        }
    }
}

@Composable
fun DoctorInfoCardItem(info: DoctorInfoCard) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(info.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(info.icon, contentDescription = info.title, tint = info.color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(info.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF333333))
                Text(info.value, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun DoctorBottomNavigationBar() {
    val selectedColor = Color(0xFF2E7D32)
    val unselectedColor = Color(0xFF9E9E9E)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                Icons.Filled.Home to "Home",
                Icons.Filled.Notifications to "Notifications",
                Icons.Outlined.Person to "Profile"
            )

            navItems.forEachIndexed { index, (icon, label) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { /* Add navigation logic here */ }
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = if (index == 0) selectedColor else unselectedColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        label,
                        color = if (index == 0) selectedColor else unselectedColor,
                        fontSize = 11.sp,
                        fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

data class DoctorInfoCard(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)