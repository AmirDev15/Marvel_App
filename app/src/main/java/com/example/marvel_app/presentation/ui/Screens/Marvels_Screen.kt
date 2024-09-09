package com.example.marvel_app.presentation.ui.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.marvel_app.domain.model.Character
import com.example.marvel_app.domain.model.Marvels_Data
import com.example.marvel_app.presentation.viewmodel.Marvels_Screen
import com.example.marvel_app.presentation.viewmodel.CharacterViewModel



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MarvelComicsScreen(
    characterId: Int,
    View_Model: Marvels_Screen,
    viewModel: CharacterViewModel,
    navController: NavController,
) {
    val character_for_detail by viewModel.character_for_details.collectAsState()

    val isLoading by View_Model.isLoading.collectAsState()

    val ViewModel_search = View_Model
    val Comics by View_Model.comics.collectAsState(emptyList())
    val Series by View_Model.series.collectAsState(emptyList())
    val Events by View_Model.events.collectAsState(emptyList())

    var expandedCategory by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(characterId) {
        View_Model.fetchComicsAndSeries(characterId)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        when (expandedCategory) {
            "Comics" -> ExpandedCategoryView("Comics", Comics) { expandedCategory = null }
            "Series" -> ExpandedCategoryView("Series", Series) { expandedCategory = null }
            "Events" -> ExpandedCategoryView("Events", Events) { expandedCategory = null }
            else -> CollapsedView(
                Comics = Comics,
                Series = Series,
                Events = Events,
                characters = character_for_detail,
                View_Model = ViewModel_search,
                navController = navController,
                isLoading = isLoading,
                onExpandCategory = { category -> expandedCategory = category },

                )
        }
    }
}

@Composable
fun CollapsedView(
    Comics: List<Marvels_Data>,
    Series: List<Marvels_Data>,
    Events: List<Marvels_Data>,
    characters: List<Character>,
    navController: NavController,
    isLoading: Boolean,
    View_Model: Marvels_Screen,

    onExpandCategory: (String) -> Unit,

    ) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(start = 5.dp, end = 5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            SearchBar(
                navController = navController,
                onSearchQueryChanged = { query -> View_Model.events },
                enabled = false,
                pop_back_stack = false,
                pop_back_stack2 = true,
                focus = false
            )
        }

        items(characters) { character ->
            CharacterItem(character)
        }

        item {

            if (isLoading) {
                Text(
                    "Loading Comics...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else if (characters.isEmpty()) {

                Text(
                    "No Comics found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {

                CategorySection(
                    title = "Comics",
                    items = Comics,
                    onExpandClick = { onExpandCategory("Comics") })
            }

        }
        item {
            if (isLoading) {
                Text(
                    "Loading Series...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else if (characters.isEmpty()) {

                Text(
                    "No Series found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                CategorySection(
                    title = "Series",
                    items = Series,
                    onExpandClick = { onExpandCategory("Series") })
            }


        }
        item {
            if (isLoading) {
                Text(
                    "Loading Events...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else if (characters.isEmpty()) {

                Text(
                    "No Events found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                CategorySection(
                    title = "Events",
                    items = Events,
                    onExpandClick = { onExpandCategory("Events") })
            }

        }

    }
}

@Composable
fun CharacterItem(character: Character) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(character.imageUrl)
                    .listener(onError = { _, result ->
                        Log.e(
                            "ImageLoading", "Error loading image: ${result.throwable.message}"
                        )
                    }, onSuccess = { _, _ ->
                        Log.d("ImageLoading", "Image loaded successfully")
                    }).build(),
                contentDescription = character.name,
                modifier = Modifier.size(80.dp),

                )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = character.name)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = character.description)
            }
        }
    }
}

@Composable
fun ExpandedCategoryView(
    title: String,
    items: List<Marvels_Data>,
    onCollapseClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCollapseClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) { item ->
                CategoryItem(item)
            }
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    items: List<Marvels_Data>,
    onExpandClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = onExpandClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "View All", color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(items) { item ->
                CategoryItem(item)
            }
        }
    }
}

@Composable
fun CategoryItem(item: Marvels_Data) {
    Box(
        modifier = Modifier
            .width(190.dp)
            .height(180.dp)
            .border(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = item.title,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
