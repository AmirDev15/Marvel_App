package com.example.marvel_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marvel_app.presentation.ui.Screens.MarvelComicsScreen
import com.example.marvel_app.presentation.ui.Screens.MarvelHomePage
import com.example.marvel_app.presentation.ui.Screens.MarvelScreen
import com.example.marvel_app.presentation.viewmodel.Marvels_Screen
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel


@Composable
fun Navigation(viewModel: CharacterViewModel, Marvels_Screen: Marvels_Screen) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home Screen") {
        composable("Home Screen") {
            MarvelHomePage(navController = navController)
        }
        composable("Search Screen") {
            MarvelScreen(viewModel = viewModel, navController = navController)
        }
        composable("Comic Screen/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toInt() ?: 0
            MarvelComicsScreen(characterId = characterId, View_Model = Marvels_Screen,viewModel = viewModel,navController = navController)
        }

    }
}



