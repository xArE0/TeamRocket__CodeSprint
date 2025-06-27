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

    // List of screens allowed without authentication
    listOf(
        RouteLoginScreen::class.qualifiedName,
        RouteSignupScreen::class.qualifiedName
    )

    NavHost(navController = navController, startDestination = RouteHomepage::class.qualifiedName ?: "") {
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
    }
}

/*-------------------------------------------Serializable Routes Objects--------------------------------------------------*/
@Serializable
object RouteHomepage

@Serializable
object RouteLoginScreen

@Serializable
object RouteSignupScreen
