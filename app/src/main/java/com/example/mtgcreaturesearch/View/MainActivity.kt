package com.example.mtgcreaturesearch.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtgcreaturesearch.View.ui.theme.MTGCreatureSearchTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.ViewModel.CardViewModel
import com.example.mtgcreaturesearch.View.BrowseScreen
import com.example.mtgcreaturesearch.ViewModel.CardUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cardViewModel: CardViewModel = viewModel()
            NavHost(navController, startDestination = "homeScreen") {
                composable("homeScreen") { HomeScreen(navController) }
                composable("browseScreen") { BrowseScreen(cardViewModel) }
                composable("favoritesScreen") { FavoritesScreen(cardUiState = cardViewModel.cardUiState) }
                composable("FilterBar"){}
            }
        }
    }
}

@Composable
fun Title(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            searchQuery = newValue
        },
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        placeholder = {
            Text(text = "Search...")
        }
    )
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title and Divider
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

        // Image
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your image resource ID
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        // Search Bar
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp))

        // Main Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Red)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Green)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }

        // Browse Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Gray)
                .clickable {
                    navController.navigate("browseScreen")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Browse",
                color = Color.White
            )
        }

        // View Favorites Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Gray)
                .clickable {
                    navController.navigate("favoritesScreen")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "View favorites",
                color = Color.White
            )
        }



        // Bottom Tab Bar in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 145.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Replace the boxes with vector assets using Image and painterResource
            Image(
                painter = painterResource(id = R.drawable.burgermenu),
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
                painter = painterResource(id = R.drawable.favorite),
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



//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val navController = rememberNavController() // Create a mock NavController
//    MTGCreatureSearchTheme {
//        HomeScreen(navController = navController, cardUiState = cardViewModel.cardUiState) // Pass the NavController
//    }
//}
