package com.example.hackathon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// User data class
data class User(
    val fullName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val imageUrl: String? = null // Add this field
)

// Cattle data class
data class Cattle(
    val tagNo: String = "",
    val type: String = "",
    val gender: String = "",
    val breed: String = "",
    val dob: String = "",
    val tbDate: String = "",
    val brDate: String = "",
    val purpose: String = "",
    val weight: String = "",
    val color: String = "",
    val notes: String = ""
)

/*-------------------------------------------Main Class to Fetch Data from Firebase-----------------------------------*/
class FirebaseDataClass {
    private val firestore = FirebaseFirestore.getInstance()

    // Function to fetch user data
    suspend fun fetchUserData(userId: String): User? {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                val data = doc.data
                User(
                    fullName = data?.get("fullName") as? String ?: "",
                    email = data?.get("email") as? String ?: "",
                    mobileNumber = data?.get("mobileNumber") as? String ?: "",
                    imageUrl = data?.get("imageUrl") as? String
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // Function to add cattle data
    suspend fun addCattle(userId: String, cattle: Cattle) {
        firestore.collection("users")
            .document(userId)
            .collection("cattle")
            .add(cattle)
            .await()
    }

    // Function to fetch cattle list
    suspend fun fetchCattleList(userId: String): List<Cattle> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("cattle")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Cattle::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveImageUrl(userId: String, imageUrl: String) {
        firestore.collection("users").document(userId)
            .update("imageUrl", imageUrl)
            .await()
    }
}

/*-------------------------------------------Navigate to Homepage Through Splash-------------------------------------*/
@Composable
fun SplashScreenApp() {
    var isLoading by remember { mutableStateOf(true) }

    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModel.Factory)

    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }

    if (isLoading) {
        SplashScreen()
    } else {
        CompositionLocalProvider(LocalSessionManager provides sessionViewModel) {
            MyAppNavigation(sessionViewModel = sessionViewModel)
        }
    }
}

/*-------------------------------------------Splash Screen UI--------------------------------------------------*/
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.homepage_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Whatever",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF69B4), // Neon Pink
                            Color(0xFF4C4CFF), // Neon Blue
                            Color(0xFF00FF00)  // Neon Green
                        )
                    ),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
