package com.example.mtgcreaturesearch.View

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.View.ui.theme.MTGCreatureSearchTheme
import com.example.mtgcreaturesearch.ViewModel.CardUiState

@Composable
fun FavoritesScreen(navController: NavController, cardUiState: CardUiState) {
    var cards: MutableList<ShownCards> = mutableListOf()
    when (cardUiState) {
        is CardUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is CardUiState.Success ->
            for (i in 0 until cardUiState.photos.size) {
                val card =
                    ShownCards(cardUiState.photos[i].image_uris.png, cardUiState.photos[i].id)
                cards.add(i, card)
            }

        CardUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_favorites),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 55.dp)
        ) {
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

            Box(modifier = Modifier.padding(5.dp)) {
                CardGrid(cards = cards, favorite = true)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(55.dp)
        ) {
            // BottomTabBar Background
            Image(
                painter = painterResource(id = R.drawable.favorites_tabbar_background),
                contentDescription = "Tab Bar Background",
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
                // Home Tab
                Image(
                    painter = painterResource(id = R.drawable.burgermenu_hvid),
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate("homeScreen") }
                )

                // Browse Tab
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Browse",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate("browseScreen") }
                )

                // Favorites Tab
                Image(
                    painter = painterResource(id = R.drawable.favorite_hvid),
                    contentDescription = "Favorites",
                    modifier = Modifier.size(30.dp) // Not clickable since it's the current screen
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FavoriteScreenPreview() {
//    MTGCreatureSearchTheme {
//        BrowseScreen()
//    }
//}