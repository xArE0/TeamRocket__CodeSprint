package com.example.hackathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun CattleDetailsPage(
    navController: NavController,
    tagNo: String
) {
    // Get sessionViewModel and sessionState from LocalSessionManager
    val sessionViewModel = LocalSessionManager.current
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val userId = sessionState.userId

    var cattle by remember { mutableStateOf<Cattle?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch cattle by tagNo
    LaunchedEffect(tagNo, userId) {
        isLoading = true
        if (!userId.isNullOrEmpty()) {
            val list = FirebaseDataClass().fetchCattleList(userId)
            cattle = list.find { it.tagNo == tagNo }
        }
        isLoading = false
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Details", "Progeny", "History")

    Scaffold(
        containerColor = Color(0xFFF6F8FC),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add action */ },
                containerColor = Color(0xFFFF6D2D),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
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
                .background(Color(0xFFF6F8FC))
                .padding(innerPadding)
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB3D8F6))
                    .padding(vertical = 18.dp, horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "View Animal",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { /* TODO: More menu */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF2196F3),
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == idx) Color(0xFF2196F3) else Color.Gray,
                                fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2196F3))
                    }
                }
                cattle == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Animal not found.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                else -> {
                    when (selectedTab) {
                        0 -> DetailsTabContent(cattle!!)
                        1 -> ComingSoonCard("Progeny info coming soon.", Color(0xFF7A4EB3))
                        2 -> ComingSoonCard("History info coming soon.", Color(0xFF2196F3))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsTabContent(cattle: Cattle) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
            .padding(20.dp)
    ) {

        // Show image if available
        cattle.imageUrl?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = "Cow Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 12.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Identification Section
        SectionHeader("Identification", "More info")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Date of Birth", color = Color.Gray)
                        Text(
                            cattle.dob,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Column {
                        Text("Gender", color = Color.Gray)
                        Text(
                            cattle.gender,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tag/ID", color = Color.Gray)
                        Text(
                            cattle.tagNo,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Column {
                        Text("Breed", color = Color.Gray)
                        Text(
                            cattle.breed,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                if (cattle.weight.isNotBlank() || cattle.color.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (cattle.weight.isNotBlank()) {
                            Column {
                                Text("Weight", color = Color.Gray)
                                Text(
                                    "${cattle.weight} kg",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        if (cattle.color.isNotBlank()) {
                            Column {
                                Text("Color", color = Color.Gray)
                                Text(
                                    cattle.color,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Labels Section
        SectionHeader("Labels", "More info", showAdd = true)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                Text(
                    "There are no labels associated with this animal",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, more: String, showAdd: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showAdd) {
                Button(
                    onClick = { /* TODO: Add label */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D2D)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("+ Add Label", color = Color.White)
                }
            }
            Text(more, color = Color.Gray)
        }
    }
}
