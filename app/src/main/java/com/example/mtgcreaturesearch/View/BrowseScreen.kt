// BrowseScreen.kt
package com.example.mtgcreaturesearch.View
import androidx.compose.foundation.Image
import com.example.mtgcreaturesearch.ViewModel.CardViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.R

import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(cardViewModel: CardViewModel = viewModel(),card: ShownCards) {
    Box(contentAlignment = Alignment.TopEnd) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.fillMaxSize(),
//            .size(width = 110.dp, height = 153.dp),
            onClick = {
            }
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = card.url,
                contentDescription = "Translated description of what the image contains",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
        }

        var isFavorite by remember { mutableStateOf(cardViewModel.isFavorited(card)) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
                cardViewModel.updateFavorites(card)
            }
        ) {
            Icon(
                tint = Color.Red,

                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }
}
//val lazyPagingItems = pager.collectAsLazyPagingItems()


@Composable
fun CardGrid(cardViewModel: CardViewModel = viewModel(), cards: List<ShownCards>) {
    // [START android_compose_layouts_lazy_grid_adaptive]
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
//        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(cards) { card ->
            Card(cardViewModel, card)
        }
    }
}


@Composable
fun BrowseScreen(cardViewModel: CardViewModel = viewModel(), navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.browse_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Main content area
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Title(name = "MTG Card Organizer")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            SearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.burgermenu),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Transparent)
                    .clickable {
                        navController.navigate("filterBar")
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.backspace),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Transparent)
                    .clickable {
                        navController.navigate("HomeScreen")
                    }
            )

            CardGrid(cards = cardViewModel.browseCards())

            // Spacer to push bottom bar to the bottom of the screen
            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom Tab Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.browse_bottom_background),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                    Image(
                        painter = painterResource(id = R.drawable.burgermenu_hvid),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Transparent)
                            .clickable {
                                // Navigate to favorites screen when favorites is clicked
                                navController.navigate("HomeScreen")
                            }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Transparent)
                            .clickable {
                                navController.navigate("BrowseScreen")
                            }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.favorite_hvid),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Transparent)
                            .clickable {
                                navController.navigate("favoritesScreen")
                            }
                    )
                }
            }

        }
    }
@Composable
fun ResultScreen(photos: List<Data>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        LazyColumn {
            // Adds up to size of photos

//            items(photos.size) { index ->
//                Text(text = "${photos.get(index).image_uris.png}")
//            }
        }
    }

}
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}
//@Preview(showBackground = true)
//@Composable
//fun BrowseScreenPreview() {
//    MTGCreatureSearchTheme {
//        BrowseScreen(cardUiState = cardUiState.error)
//    }
//}
//:)