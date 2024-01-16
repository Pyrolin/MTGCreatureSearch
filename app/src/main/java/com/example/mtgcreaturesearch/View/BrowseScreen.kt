// BrowseScreen.kt
package com.example.mtgcreaturesearch.View
import CardApi
import CardApiService
import android.util.Log
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
import androidx.compose.foundation.layout.width
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
import com.example.mtgcreaturesearch.Model.Query
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.rotate
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.example.mtgcreaturesearch.ViewModel.CardRepository
import com.example.mtgcreaturesearch.ViewModel.TestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(cardViewModel: CardViewModel = viewModel(), navController: NavController, card: ShownCards, setonClick: Boolean = true, flipped: Boolean = false) {
    Box(contentAlignment = Alignment.TopEnd) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            onClick = {
                if (setonClick) {
                    navController.navigate("cardScreen/${card.id}")
                }
            }
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate((if (flipped && card.layout == "flip") 180 else 0).toFloat()),
                model = if (flipped && card.layout != "flip") card.card_faces?.get(1)?.image_uris?.large else card.url,
                contentDescription = "Image of the creature card",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
        }

        var isFavorite by remember { mutableStateOf(false) }
        isFavorite = cardViewModel.isFavorited(card)

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


@Composable
fun CardGrid(cardViewModel: CardViewModel = viewModel(), navController: NavController,  cards: List<ShownCards>) {
    // [START android_compose_layouts_lazy_grid_adaptive]
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
//        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
        modifier = Modifier
            .fillMaxSize()
            .fillMaxSize()
            .padding(bottom = 55.dp)
    ) {
        items(cards) { card ->
            Card(cardViewModel, navController, card)
        }
    }
}

@Composable
fun PagingListScreen(cardViewModel: CardViewModel) {
    val viewModel = TestViewModel(CardRepository(CardApi))
    val cards = viewModel.getPaginationCards().collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(
            count = cards.itemCount
        ) { index ->
            val item = cards[index]
            if (item == null) {
                Log.d("LIST", "no item")
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = if (item!!.image_uris != null) item.image_uris?.large else item.card_faces?.get(0)?.image_uris?.large,
                contentDescription = "Image of the creature card",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
        }
        cards.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator(color = Color.Black) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = cards.loadState.refresh as LoadState.Error
                    item {
                        Log.e("REFRESH ERROR", error.toString())

                    }
                }

                loadState.append is LoadState.Loading -> {
                    item { Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Pagination Loading")

                        CircularProgressIndicator(color = Color.Black)
                    } }
                }

                loadState.append is LoadState.Error -> {
                    val error = cards.loadState.append as LoadState.Error
                    item {
                        Log.e("APPEND ERROR", error.toString())
                    }
                }
            }
        }
    }
/*
    LazyColumn() {
        items(cards.itemCount) { index ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = cards[index]!!.url,
                contentDescription = "Image of the creature card",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
            )
        }
        cards.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator(color = Color.Black) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = cards.loadState.refresh as LoadState.Error
                    item {
                        Log.e("REFRESH ERROR", error.toString())

                    }
                }

                loadState.append is LoadState.Loading -> {
                    item { Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Pagination Loading")

                        CircularProgressIndicator(color = Color.Black)
                    } }
                }

                loadState.append is LoadState.Error -> {
                    val error = cards.loadState.append as LoadState.Error
                    item {
                        Log.e("APPEND ERROR", error.toString())
                    }
                }
            }
        }
    }
    */
}


@Composable
fun BrowseScreen(cardViewModel: CardViewModel, navController: NavController, order: String = "", q: String = "") {
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

           SearchBar(cardViewModel ,modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
               navController,
               reloadPage = true
           )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                Image(
                    painter = painterResource(id = R.drawable.burgermenu),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Transparent)
                        .clickable {
                            navController.navigate("filterBar/browseScreen")
                        }
                )
            }

            val query = Query(order,q)
            //val browseCards = cardViewModel.browseCards(query)
            PagingListScreen(cardViewModel)
            /*
            when (browseCards.isNotEmpty()) {
                true -> {
                    CardGrid(navController = navController, cards = browseCards)
                }

                else -> {
                    CardGrid(navController = navController, cards = cardViewModel.browseCards(query))
                }
            }*/

            Spacer(modifier = Modifier.weight(1f))
        }

        BottomBar(navController = navController, cardViewModel = cardViewModel, modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(55.dp),
            R.drawable.browse_bottom_background)

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