package com.example.hackathon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hackathon.ui.theme.HackathonTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            HackathonTheme {
                val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModel.Factory)
                CompositionLocalProvider(LocalSessionManager provides sessionViewModel) {
                    MyAppNavigation(sessionViewModel = sessionViewModel)
                }
            }
        }
    }
}