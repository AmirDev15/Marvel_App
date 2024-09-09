package com.example.marvel_app.presentation.ui.Screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.marvel_app.R
import kotlinx.coroutines.launch


@Composable
fun MarvelHomePage(navController: NavController) {

    var categories by remember { mutableStateOf(getInitialCategories()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.marvel_logo),
            contentDescription = "Marvel Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ImageSlider()

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            navController = navController,
            onSearchQueryChanged = {},
            enabled = false,
            pop_back_stack = false,
            false,
            focus= false

        )

        Spacer(modifier = Modifier.height(16.dp))

        MarvelCategoryGrid(categories)


        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(5000)
            categories = categories.map {
                it.copy(title = it.title)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {

    val images = listOf(
        R.drawable.marvel_wallper,
        R.drawable.wallper_2, R.drawable.wallper3, R.drawable.wallper_5
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        HorizontalPager(
            state = pagerState
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Slide Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalArrangement = Arrangement.Center
    ) {
        repeat(images.size) { index ->
            val isSelected = pagerState.currentPage == index

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .padding(horizontal = 1.dp)
                    .background(
                        if (isSelected) Color.Gray else Color.LightGray,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }

    LaunchedEffect(key1 = pagerState) {
        coroutineScope.launch {
            while (true) {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % 4)
                kotlinx.coroutines.delay(3000)
            }
        }
    }
}

@Composable
fun SearchBar(
    navController: NavController,
    onSearchQueryChanged: (String) -> Unit,
    enabled: Boolean = false,
    pop_back_stack: Boolean,
    pop_back_stack2:Boolean,
    focus: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(focus) {
        if (focus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }else{

            keyboardController?.hide()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth().height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = textState,
                    enabled = enabled,
                    onValueChange = {
                        textState = it
                        onSearchQueryChanged(it.text)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth().focusRequester(focusRequester)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (!enabled) {

                                navController.navigate("Search Screen")

                            }
                        },


                    singleLine = true
                ) {
                    if (textState.text.isEmpty()) {
                        Text(
                            text = "Search for marvel characters",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    } else {
                        it()
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {


                    if (pop_back_stack) {
                        Log.d("marvel_search", "pop_back_stack is true")
                        textState = TextFieldValue("")
                        navController.navigate("Home Screen")
                    }
                    if(pop_back_stack2){
                        textState = TextFieldValue("")
                        navController.navigate("Search Screen")
                    }
                }) {
                    Box(modifier = Modifier.background(Color.Black, shape = CircleShape)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search",
                            tint = Color.White,
                        )

                    }
                }
            }
        }
    }
}


@Composable
fun MarvelCategoryGrid(categories: List<Category>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories.size) { index ->
            CategoryCard(
                title = categories[index].title, imageRes = categories[index].imageRes
            )
        }
    }
}

@Composable
fun CategoryCard(title: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .border(width = 1.dp, color = Color.LightGray)
                .fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}

data class Category(val title: String, val imageRes: Int)


fun getInitialCategories(): List<Category> {
    return listOf(
        Category("Characters", R.drawable.characters),
        Category("Comics", R.drawable.black_comics),
        Category("Events", R.drawable.events),
        Category("Cartoons", R.drawable.cartoons),
        Category("Series", R.drawable.series),
        Category("Stories", R.drawable.story),
    )
}


