package com.example.hackathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon

/*-------------------------------------------Homepage UI--------------------------------------------------*/
@Composable
fun Homepage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    val backgroundColor = Color(0xFF9DD6F6)
    Scaffold(
        containerColor = backgroundColor,
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add main action */ },
                containerColor = Color(0xFFFF8000),
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(40.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "herdwatch",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("23°C", color = Color.White, fontWeight = FontWeight.Bold)
                    Icon(
                        Icons.Filled.Cloud,
                        contentDescription = "Weather",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Herd count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            ) {
                Icon(
                    Icons.Filled.Pets,
                    contentDescription = "Herd",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("0", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("in herd", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.offset(y = 6.dp))
            }

            // Search bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(32.dp))
                    .height(56.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(
                        "What would you like to do?",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(48.dp)
                            .background(Color(0xFFFF8000), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Mic,
                            contentDescription = "Voice",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                val gridItems = listOf(
                    Icons.Filled.CheckCircle to "Animal Records",
                    Icons.Filled.LocalDrink to "Dairy Performance",
                    Icons.Filled.Male to "Breeding",
                    Icons.Filled.Map to "Pasture & Maps",
                    Icons.Filled.Assignment to "Management",
                    Icons.Filled.Description to "Reports"
                )
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..1) {
                            val index = row * 2 + col
                            val (icon, label) = gridItems[index]
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(160.dp, 120.dp)
                                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                                    .clickable { /* TODO: Handle click */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        icon,
                                        contentDescription = label,
                                        tint = Color(0xFF0077B6),
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(label, color = Color.Black, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val quickActions = listOf(
                    Icons.Filled.Pets to "Add animal",
                    Icons.Filled.MedicalServices to "Medicine Purchase",
                    Icons.Filled.Vaccines to "Cattle Treatment"
                )
                quickActions.forEach { (icon, label) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(64.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Icon(
                                icon,
                                contentDescription = label,
                                tint = Color(0xFF0077B6),
                                modifier = Modifier.size(48.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(0xFFFF8000), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(label, color = Color.Black, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Apps, contentDescription = "Apps", tint = Color.Gray)
            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color(0xFF0077B6))
            Icon(Icons.Filled.Pets, contentDescription = "My Herd", tint = Color.Gray)
            Box {
                Icon(Icons.Filled.Assignment, contentDescription = "Watchboard", tint = Color.Gray)
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color(0xFFFF8000), shape = CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text("6", color = Color.White, fontSize = 12.sp)
                }
            }
            Icon(Icons.Filled.Help, contentDescription = "Help", tint = Color.Gray)
        }
    }
}