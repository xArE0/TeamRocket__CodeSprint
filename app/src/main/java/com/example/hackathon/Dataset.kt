package com.example.hackathon

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun DatasetPage(sessionViewModel: SessionViewModel, sessionState: SessionState) {
    val context = LocalContext.current
    val userId = sessionState.userId
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                scope.launch {
                    if (userId != null) {
                        val result = uploadCattleFromCsv(context, userId)
                        status = result
                    } else {
                        status = "User not logged in"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Cattle from CSV")
        }
        status?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it)
        }
    }
}

suspend fun uploadCattleFromCsv(context: Context, userId: String): String {
    return try {
        val inputStream = context.resources.openRawResource(R.raw.data)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        var count = 0
        reader.readLine() // skip header
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        while (reader.readLine().also { line = it } != null) {
            val tokens = line!!.split(",")
            val csvCattle = CsvCattle(
                cowId = tokens.getOrNull(0) ?: "",
                breedCsv = tokens.getOrNull(1) ?: "",
                lastHeatDate = tokens.getOrNull(2) ?: "",
                inseminationDate = tokens.getOrNull(3) ?: "",
                aiType = tokens.getOrNull(4) ?: "",
                bullInfo = tokens.getOrNull(5) ?: "",
                pregnancy = tokens.getOrNull(6) ?: "",
                expectedCalvingDate = tokens.getOrNull(7) ?: "",
                calvingDate = tokens.getOrNull(8) ?: "",
                type = tokens.getOrNull(9) ?: "",
                gender = tokens.getOrNull(10) ?: "",
                breed = tokens.getOrNull(11) ?: "",
                dob = tokens.getOrNull(12) ?: "",
                tbDate = tokens.getOrNull(13) ?: "",
                brDate = tokens.getOrNull(14) ?: "",
                purpose = tokens.getOrNull(15) ?: "",
                weight = tokens.getOrNull(16) ?: "",
                color = tokens.getOrNull(17) ?: "",
                notes = tokens.getOrNull(18) ?: "",
                imageUrl = tokens.getOrNull(19),
                startDate = tokens.getOrNull(20) ?: "",
                endDate = tokens.getOrNull(21) ?: "",
                totalMilkProduced = tokens.getOrNull(22) ?: "",
                calculatedEndDate = tokens.getOrNull(23) ?: ""
            )
            firestore.collection("users")
                .document(userId)
                .collection("cattle")
                .add(
                    mapOf(
                        "cowId" to csvCattle.cowId,
                        "breedCsv" to csvCattle.breedCsv,
                        "lastHeatDate" to csvCattle.lastHeatDate,
                        "inseminationDate" to csvCattle.inseminationDate,
                        "aiType" to csvCattle.aiType,
                        "bullInfo" to csvCattle.bullInfo,
                        "pregnancy" to csvCattle.pregnancy,
                        "expectedCalvingDate" to csvCattle.expectedCalvingDate,
                        "calvingDate" to csvCattle.calvingDate,
                        "type" to csvCattle.type,
                        "gender" to csvCattle.gender,
                        "breed" to csvCattle.breed,
                        "dob" to csvCattle.dob,
                        "tbDate" to csvCattle.tbDate,
                        "brDate" to csvCattle.brDate,
                        "purpose" to csvCattle.purpose,
                        "weight" to csvCattle.weight,
                        "color" to csvCattle.color,
                        "notes" to csvCattle.notes,
                        "imageUrl" to csvCattle.imageUrl,
                        "startDate" to csvCattle.startDate,
                        "endDate" to csvCattle.endDate,
                        "totalMilkProduced" to csvCattle.totalMilkProduced,
                        "calculatedEndDate" to csvCattle.calculatedEndDate
                    )
                )
                .await()
            count++
        }
        reader.close()
        "Uploaded $count cattle records."
    } catch (e: Exception) {
        "Error: ${e.localizedMessage}"
    }
}