package com.example.marvel_app.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.marvel_app.presentation.navigation.Navigation
import com.example.marvel_app.util.initializeDependencies

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Initialize dependencies and get the ViewModels
            val (characterViewModel, characterDetailsViewModel) = initializeDependencies(this)
            // Set the content with the initialized ViewModels
            setContent {
                Navigation(
                    viewModel = characterViewModel,
                    Marvels_Screen = characterDetailsViewModel
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainActivity", "Error initializing dependencies: ${e.message}")
        }
    }
}

