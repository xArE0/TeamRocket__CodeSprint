package com.example.hackathon

import android.net.Uri
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

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
    val notes: String = "",
    val imageUrl: String? = null,

    // New fields from Firebase
    val aiType: String? = null,
    val breedCsv: String? = null,
    val bullInfo: String? = null,
    val calculatedEndDate: String? = null,
    val calvingDate: String? = null,
    val cowId: String? = null,
    val endDate: String? = null,
    val expectedCalvingDate: String? = null,
    val inseminationDate: String? = null,
    val lastHeatDate: String? = null,
    val pregnancy: String? = null,
    val startDate: String? = null,
    val totalMilkProduced: String? = null
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
    // In ViewModal.kt, inside FirebaseDataClass
    suspend fun addCattle(userId: String, cattle: Cattle) {
        firestore.collection("users")
            .document(userId)
            .collection("cattle")
            // In FirebaseDataClass.kt, in addCattle()
            .add(
                mapOf(
                    "tagNo" to cattle.tagNo,
                    "type" to cattle.type,
                    "gender" to cattle.gender,
                    "breed" to cattle.breed,
                    "dob" to cattle.dob,
                    "tbDate" to cattle.tbDate,
                    "brDate" to cattle.brDate,
                    "purpose" to cattle.purpose,
                    "weight" to cattle.weight,
                    "color" to cattle.color,
                    "notes" to cattle.notes,
                    "imageUrl" to cattle.imageUrl,
                    // New fields
                    "aiType" to cattle.aiType,
                    "breedCsv" to cattle.breedCsv,
                    "bullInfo" to cattle.bullInfo,
                    "calculatedEndDate" to cattle.calculatedEndDate,
                    "calvingDate" to cattle.calvingDate,
                    "cowId" to cattle.cowId,
                    "endDate" to cattle.endDate,
                    "expectedCalvingDate" to cattle.expectedCalvingDate,
                    "inseminationDate" to cattle.inseminationDate,
                    "lastHeatDate" to cattle.lastHeatDate,
                    "pregnancy" to cattle.pregnancy,
                    "startDate" to cattle.startDate,
                    "totalMilkProduced" to cattle.totalMilkProduced
                )
            )
            .await()
    }

    fun saveCattleAction(
        userId: String,
        cattleId: String,
        lastHeatStart: String,
        nextExpectedHeat: String,
        lastInseminationDate: String,
        pregnancyStatus: String,
        heatCycleStage: String,
        optimalBreedingWindow: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = firestore
        val cattleDocRef = db.collection("users").document(userId)
            .collection("cattle").document(cattleId)

        val updates = mapOf(
            "lastHeatDate" to lastHeatStart,
            "expectedHeatDate" to nextExpectedHeat,
            "inseminationDate" to lastInseminationDate,
            "pregnancy" to pregnancyStatus,
            "heatCycleStage" to heatCycleStage,
            "optimalBreedingWindow" to optimalBreedingWindow
        )

        cattleDocRef.update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Unknown error") }
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

    //Sensitive Info
    private val imgbbApiKey = "a6e0328f93227efb26846c08025e6749"

    suspend fun saveImageUrl(userId: String, imageUrl: String) {
        firestore.collection("users").document(userId)
            .update("imageUrl", imageUrl)
            .await()
    }

    suspend fun uploadImageToImgbb(context: android.content.Context, uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@withContext null
                val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                val client = OkHttpClient()
                val requestBody = FormBody.Builder()
                    .add("key", imgbbApiKey)
                    .add("image", base64)
                    .build()
                val request = Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val json = org.json.JSONObject(responseBody)
                    json.getJSONObject("data").getString("url")
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
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
