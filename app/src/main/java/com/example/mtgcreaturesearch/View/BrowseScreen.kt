// BrowseScreen.kt
package com.example.mtgcreaturesearch.View

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.ViewModel.CardViewModel
import forest
import island
import mana
import mountain
import plains
import power
import swamp
import textSearch
import toughness

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    cardViewModel: CardViewModel = viewModel(),
    navController: NavController,
    card: ShownCards,
    setonClick: Boolean = true,
    flipped: Boolean = false
) {
    Box(contentAlignment = Alignment.TopEnd) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            onClick = {
                if (setonClick) {
                    test_card = ShownCards(
                        card.url,
                        card.id,
                        card.toughness,
                        card.power,
                        card.cmc,
                        card.layout,
                        card.colors,
                        card.oracle_text,
                        card.card_faces
                    )
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

        val favoriteSize = if (setonClick) 25.dp else 50.dp

        val favoriteXOffset = if (setonClick) 0.dp else (-10).dp
        val favoriteYOffset = if (setonClick) 0.dp else (-60).dp

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
                cardViewModel.updateFavorites(card)
            },
            modifier = Modifier.size(favoriteSize)
                .absoluteOffset(x=favoriteXOffset, y=favoriteYOffset)
        ) {
            Icon(
                tint = if (isFavorite || setonClick) {
                    Color.Red
                } else {
                    Color.hsv(0F, 0.8F, 0.4F)
                },
                imageVector = if (isFavorite || !setonClick) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(favoriteSize)
            )
        }
    }
}


@Composable
fun CardGrid(
    cardViewModel: CardViewModel = viewModel(),
    navController: NavController,
    cards: List<ShownCards>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
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
fun PagingCardGrid(navController: NavController, cardViewModel: CardViewModel) {

    val cards = cardViewModel.getPaginationCards().collectAsLazyPagingItems()

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(
            count = cards.itemCount
        ) { index ->
            val item = cards[index]
            if (item == null) {
                Log.d("LIST", "no item")
            } else {
                var image = item.image_uris?.large
                if (image == null) {
                    image = item.card_faces?.get(0)?.image_uris?.large
                }
                image?.let {
                    ShownCards(
                        it,
                        item.id,
                        item.toughness,
                        item.power,
                        item.cmc,
                        item.layout,
                        item.colors,
                        item.oracle_text,
                        item.card_faces
                    )
                }
                    ?.let {
                        Card(
                            cardViewModel, navController,
                            it
                        )
                    }
            }
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
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Pagination Loading")

                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
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
}


@Composable
fun BrowseScreen(
    cardViewModel: CardViewModel = viewModel(),
    navController: NavController,
    order: String = "",
    q: String = ""
) {
    val state = rememberScrollState()
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
                    .verticalScroll(state)
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

            SearchBar(
                cardViewModel, modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                navController,
                reloadPage = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp, horizontal = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backspace),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                        .clickable {
                            navController.navigate("HomeScreen")
                        }
                )

                val iconID: Int =
                    if (mana == "" && toughness == "" && power == "" && !swamp && !plains && !island && !mountain && !forest && textSearch == "") {
                        R.drawable.filter_lines_off
                    } else {
                        R.drawable.filter_lines_on
                    }

                Image(
                    painter = painterResource(id = iconID),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                        .clickable {
                            navController.navigate("filterBar/browseScreen")
                        }
                )

            }

            PagingCardGrid(navController, cardViewModel)

            Spacer(modifier = Modifier.weight(1f))
        }

        BottomBar(
            navController = navController, cardViewModel = cardViewModel, modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(55.dp),
            R.drawable.browse_bottom_background
        )

    }
}