// BrowseScreen.kt
package com.example.mtgcreaturesearch.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mtgcreaturesearch.Model.Creature
import com.example.mtgcreaturesearch.Model.Creatures
import com.example.mtgcreaturesearch.View.ui.theme.MTGCreatureSearchTheme
import okhttp3.internal.notifyAll

val cardsExample = Creature("Creature1", true);
val cardsExample2 = Creature("Creature2", false);
val cardsExample3 = Creature("Creature3", false);
val cardsExample4 = Creature("Creature4", true);
val cardsExample5 = Creature("Creature5", false);

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(card: Creature) {
    var favorited by remember {
        mutableStateOf(if (card.getFavorited()) "<3" else "")
    }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 110.dp, height = 153.dp),
        onClick = {
            card.setFavorited(!card.getFavorited())
            favorited = if (card.getFavorited()) "<3" else ""
        }
    ) {
        Text(
            text = card.getName(),
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = favorited,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CardGrid(cards: Creatures, favorite: Boolean) {
    var favorites by remember { mutableStateOf(cards.element.filter { if(favorite) it.getFavorited() else true } ) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(110.dp),
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
    ) {
        items(favorites) { card ->
            Card(card)
        }
    }
}


@Composable
fun BrowseScreen(elements: Creatures) {
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
            .padding(16.dp))

        Box(
            modifier = Modifier
            .padding(5.dp),
        ) {
            CardGrid(cards = elements, favorite = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrowseScreenPreview() {
    MTGCreatureSearchTheme {
        //BrowseScreen(elements)
    }
}
