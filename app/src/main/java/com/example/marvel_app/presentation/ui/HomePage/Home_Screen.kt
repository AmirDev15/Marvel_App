package com.example.marvel_app.presentation.ui.HomePage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvel_app.R
import kotlinx.coroutines.launch


@Composable
fun MarvelHomePage() {
    // Move the initialization of categories here
    var categories by remember { mutableStateOf(getInitialCategories()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.imgg),
            contentDescription = "Marvel Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ImageSlider()

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar()

        Spacer(modifier = Modifier.height(16.dp))

        MarvelCategoryGrid(categories)

        // Simulate a title change after 5 seconds
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(5000)
            categories = categories.map {
                it.copy(title = it.title )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        HorizontalPager(
            state = pagerState
        ) {
            Image(
                painter = painterResource(id = R.drawable.imgg), // Replace with actual image resources
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
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) { index ->
            val isSelected = pagerState.currentPage == index

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .padding(horizontal = 1.dp)
                    .background(if (isSelected) Color.Gray else Color.LightGray, shape = RoundedCornerShape(50))
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
fun SearchBar() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.weight(1f),
            singleLine = true,
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
                title = categories[index].title,
                imageRes = categories[index].imageRes
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
                    contentScale = ContentScale.Crop,
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

// Note: getInitialCategories is not a Composable function
fun getInitialCategories(): List<Category> {
    return listOf(
        Category("Characters", R.drawable.imgg),
        Category("Comics", R.drawable.imgg),
        Category("Events", R.drawable.imgg),
        Category("Cartoons", R.drawable.imgg),
        Category("Series", R.drawable.imgg),
        Category("Stories", R.drawable.imgg),
    )
}

@Preview(showBackground = true)
@Composable
fun MarvelHomePagePreview() {
    MarvelHomePage()
}