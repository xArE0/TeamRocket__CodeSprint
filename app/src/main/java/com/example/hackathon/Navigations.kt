package com.example.hackathon

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

/*-------------------------------------------Routes and Navigation--------------------------------------------------*/
@Composable
fun MyAppNavigation(
    sessionViewModel: SessionViewModel = LocalSessionManager.current
) {
    val navController = rememberNavController()
    val sessionState by sessionViewModel.sessionState.collectAsState()

    val startDestination = when {
        !sessionState.isAuthenticated -> RouteLoginScreen::class.qualifiedName!!
        sessionState.isVet == true -> RouteVetProfile::class.qualifiedName!!
        else -> RouteHomepage::class.qualifiedName!!
    }

    // Redirect to login if not authenticated
    LaunchedEffect(sessionState.isAuthenticated) {
        if (!sessionState.isAuthenticated) {
            navController.navigate(RouteLoginScreen::class.qualifiedName ?: "") {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
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
                sessionViewModel = sessionViewModel,
                sessionState = sessionState
            )
        }
        composable<RouteSignupScreen> {
            SignupScreen(
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
            VetProfileScreen(
                navController = navController

            )
        }
        composable<AddCattleScreen> {
            AddNewAnimalScreen(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }
        composable<RouteDetailsPage> {
            val parameters = it.toRoute<RouteDetailsPage>()
            CattleDetailsPage(
                navController = navController,
                tagNo = parameters.tag
            )
        }
        composable<RouteVetSignup> {
            VetSignupScreen(
                navController = navController
            )
        }
        composable<RouteVetProfile> {
            DoctorProfileScreen(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }
        composable<RouteVetNot> {
            DoctorNotificationScreen()
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

@Serializable
object AddCattleScreen

@Serializable
data class RouteDetailsPage(
    val tag: String
)
@Serializable
object RouteVetSignup

@Serializable
object RouteVetProfile

@Serializable
object RouteVetNot