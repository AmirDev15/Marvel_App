package com.example.marvel_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marvel_app.presentation.ui.Screens.CharacterDetailsScreen
import com.example.marvel_app.presentation.ui.Screens.MarvelHomePage
import com.example.marvel_app.presentation.ui.Screens.Search
import com.example.marvel_app.presentation.viewmodel.CharacterDetailsViewModel
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel


@Composable
fun Navigation(viewModel: CharacterViewModel, CharacterDetailsViewModel: CharacterDetailsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home Screen") {
        composable("Home Screen") {
            MarvelHomePage(navController = navController)
        }
        composable("Search Screen") {
            Search(viewModel = viewModel, navController = navController)
        }
        composable("Comic Screen/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toInt() ?: 0
            CharacterDetailsScreen(characterId = characterId, viewmodel = CharacterDetailsViewModel,viewModel = viewModel,navController = navController)
        }

    }
}



