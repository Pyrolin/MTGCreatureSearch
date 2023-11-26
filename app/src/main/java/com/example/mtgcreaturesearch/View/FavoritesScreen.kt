package com.example.mtgcreaturesearch.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.View.ui.theme.MTGCreatureSearchTheme
import com.example.mtgcreaturesearch.ViewModel.CardUiState

@Composable
fun FavoritesScreen(cardUiState: CardUiState) {
    var cards: MutableList<ShownCards> = mutableListOf()
    when (cardUiState){
        is CardUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is CardUiState.Success ->
            for (i in 0.. cardUiState.photos.size-1){
                val card = ShownCards(cardUiState.photos[i].image_uris.png, cardUiState.photos[i].id)
                cards.add(i, card)
            }
//        is CardUiState.Success -> ResultScreen(
//            cardUiState.photos, modifier = Modifier.fillMaxWidth()
//        )
        CardUiState.Error -> ErrorScreen( modifier = Modifier.fillMaxSize())
        else -> {}
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Title(name = "MTG Card Organizer")
        }

        // Add a divider bar under the title
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )

        Box(
            modifier = Modifier
                .padding(5.dp)
        ) {
            CardGrid(
                cards = cards, favorite = true
            )
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