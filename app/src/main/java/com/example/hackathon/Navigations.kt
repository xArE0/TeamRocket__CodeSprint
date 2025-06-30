package com.example.hackathon

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

/*-------------------------------------------Routes and Navigation--------------------------------------------------*/
@Composable
fun MyAppNavigation(
    sessionViewModel: SessionViewModel = LocalSessionManager.current
) {
    val navController = rememberNavController()
    val sessionState by sessionViewModel.sessionState.collectAsState()

    // Redirect to login if not authenticated
    LaunchedEffect(sessionState.isAuthenticated) {
        if (!sessionState.isAuthenticated) {
            navController.navigate(RouteLoginScreen::class.qualifiedName ?: "") {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = if (sessionState.isAuthenticated) RouteHomepage::class.qualifiedName!! else RouteLoginScreen::class.qualifiedName!!) {
        composable<RouteHomepage> {
            Homepage(
                navController = navController,
                sessionViewModel = sessionViewModel,
                sessionState = sessionState
            )
        }
        composable<RouteLoginScreen> {
            LoginScreen(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }
        composable<RouteSignupScreen> {
            Signup(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }
        composable<RouteHerdPage> {
            HerdPage(
                navController = navController,
                sessionViewModel = sessionViewModel,
                sessionState = sessionState
            )
        }
        composable<RouteTasksPage> {
            TasksPage(
                navController = navController,
                sessionViewModel = sessionViewModel,
                sessionState = sessionState
            )
        }
        composable<RouteProfilePage> {
            ProfilePage(
                navController = navController,
                sessionViewModel = sessionViewModel,
                sessionState = sessionState
            )
        }
    }
}
/*-------------------------------------------Serializable Routes Objects--------------------------------------------------*/
@Serializable
object RouteHomepage

@Serializable
object RouteLoginScreen

@Serializable
object RouteSignupScreen

@Serializable
object RouteHerdPage

@Serializable
object RouteTasksPage

@Serializable
object RouteProfilePage