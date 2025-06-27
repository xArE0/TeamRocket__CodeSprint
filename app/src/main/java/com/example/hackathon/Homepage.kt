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
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun Homepage(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    sessionState: SessionState
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome to the Homepage!",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            // Add more content here as needed
        }
    }
}

/*-------------------------------------------Bottom Navigation Bar--------------------------------------------------*/
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val surfaceColor = MaterialTheme.colorScheme.surface

    val items = listOf(
        Triple(R.drawable.home_icon, "Home", RouteHomepage),
        Triple(R.drawable.menu, "Signup", RouteSignupScreen),
        Triple(R.drawable.saved_icon, "Login", RouteLoginScreen),
    )

    //Bottom Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(bottom = 12.dp)
    ) {
        // Bottom Navigation Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
        )

        // Navigation Items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, (icon, label, route) ->
                if (index == 2) {
                    Spacer(modifier = Modifier.width(80.dp).clip(RoundedCornerShape(16.dp)))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                ) {
                    val selected = currentRoute == route
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = label,
                        colorFilter = if (selected) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(RouteSignupScreen) {
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-34).dp),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 15.dp
            )
        ) {
            Image(
                painter = painterResource(R.drawable.add_icon),
                contentDescription = "Add Button",
                modifier = Modifier.size(64.dp)
            )
        }

    }
}
