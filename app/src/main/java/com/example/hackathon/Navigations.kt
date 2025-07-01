// Navigations.kt (fixed with inline Notification selection)
package com.example.hackathon

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun MyAppNavigation(
    sessionViewModel: SessionViewModel = LocalSessionManager.current
) {
    val navController = rememberNavController()
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val selectedNotification = remember { mutableStateOf<CattleNotificationDetailed?>(null) }

    LaunchedEffect(sessionState.isAuthenticated) {
        if (!sessionState.isAuthenticated) {
            navController.navigate(RouteLoginScreen::class.qualifiedName ?: "") {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (sessionState.isAuthenticated)
            RouteHomepage::class.qualifiedName!!
        else
            RouteLoginScreen::class.qualifiedName!!
    ) {
        composable<RouteHomepage> {
            Homepage(navController, sessionViewModel, sessionState)
        }
        composable<RouteLoginScreen> {
            LoginScreen(navController, sessionViewModel)
        }
        composable<RouteSignupScreen> {
            Signup(navController, sessionViewModel)
        }
        composable<RouteHerdPage> {
            HerdPage(navController, sessionViewModel, sessionState)
        }
        composable<RouteTasksPage> {
            DoctorProfileScreen(navController, sessionViewModel)
        }
        composable<RouteProfilePage> {
            DoctorNotificationScreen(
                navController = navController,
                onNotificationClick = { notification ->
                    selectedNotification.value = notification
                    navController.navigate(RouteNotificationLandScreen::class.qualifiedName!!)
                }
            )
        }
        composable<AddCattleScreen> {
            AddNewAnimalScreen(navController, sessionViewModel)
        }
        composable<RouteNotificationLandScreen> {
            val notification = selectedNotification.value
            if (notification != null) {
                NotificationDetailScreen(
                    navController = navController,
                    sessionViewModel = sessionViewModel,
                    notification = notification
                )
            } else {
                Text("No notification selected.")
            }
        }
    }
}

// Serializable Routes
@Serializable object RouteHomepage
@Serializable object RouteLoginScreen
@Serializable object RouteSignupScreen
@Serializable object RouteHerdPage
@Serializable object RouteTasksPage
@Serializable object RouteProfilePage
@Serializable object AddCattleScreen
@Serializable object RouteNotificationLandScreen
