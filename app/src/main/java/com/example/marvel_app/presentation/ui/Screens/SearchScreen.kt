package com.example.marvel_app.presentation.ui.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvel_app.data.framework.util.NetworkStateMessage
import com.example.marvel_app.data.framework.util.checkIfOnline
import com.example.marvel_app.domain.entity.Character
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun Search(viewModel: CharacterViewModel, navController: NavController) {
    val context = LocalContext.current
    val characters by viewModel.characters.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    Log.d("CharacterViewModel", "characters: $characters")
    Log.d("CharacterViewModel", "loading view: $loadingState")






    Scaffold(topBar = {
        TopAppBar(title = { Text("Marvel Characters") })
    }) {


        Column(
            modifier = Modifier
                .padding(top = 65.dp)
                .fillMaxSize()
                .padding(5.dp)
        ) {
            SearchBar(
                navController = navController,
                onSearchQueryChanged = { searchQuery -> viewModel.onSearchQueryChanged(searchQuery) },
                enabled = true,
                pop_back_stack = true,
                pop_back_stack2 = false,
                focus = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!checkIfOnline(context)) {
                Log.d("RepositoryImpl", "No internet connection")
                NetworkStateMessage(context)
            }
            if (loadingState) {

                Text(
                    "Loading character...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (characters.isEmpty()) {

                Text(
                    "No characters found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {

                LazyColumn {
                    items(characters) { character ->
                        CharacterItem(character, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character, navController: NavController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {

            navController.navigate("Comic Screen/${character.id}")
        }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = character.name)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
