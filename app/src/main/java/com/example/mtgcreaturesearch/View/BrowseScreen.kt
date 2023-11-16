// BrowseScreen.kt
package com.example.mtgcreaturesearch.View
import CardApiService
import androidx.compose.foundation.Image
import com.example.mtgcreaturesearch.ViewModel.CardViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.View.ui.theme.MTGCreatureSearchTheme
import com.example.mtgcreaturesearch.ViewModel.CardUiState


val cardsExample = listOf("Card1", "Card2", "Card3", "Card4", "Card5")
@Composable
fun Card(url: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.fillMaxSize()
//            .size(width = 110.dp, height = 153.dp)
    ) {
//        Text(
//            text = title,
//            modifier = Modifier
//                .padding(16.dp),
//            textAlign = TextAlign.Center,
//        )
        AsyncImage(
            model = "${url}",
            contentDescription = "Translated description of what the image contains",
            alignment = Alignment.Center,

        )
    }
}

@Composable
fun CardGrid(cards: List<String>) {
    // [START android_compose_layouts_lazy_grid_adaptive]
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),

//        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
        modifier = Modifier.fillMaxSize()

    ) {
        items(cards) { card ->
            Card(card)
        }
    }
}


@Composable
fun BrowseScreen(cardUiState: CardUiState) {
    var urlStrings: MutableList<String> = mutableListOf()
    when (cardUiState){
        is CardUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is CardUiState.Success ->
            for (i in 0.. cardUiState.photos.size-1){
            urlStrings.add (i,cardUiState.photos[i].image_uris.png)
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

        // Add the search bar
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        )
        CardGrid(
            cards = (urlStrings)
        )
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
