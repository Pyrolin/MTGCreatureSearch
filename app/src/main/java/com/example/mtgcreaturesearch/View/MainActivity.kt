package com.example.mtgcreaturesearch.View

import SearchFilter
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.ViewModel.CardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cardViewModel: CardViewModel = viewModel()

            NavHost(navController, startDestination = "homeScreen") {
                composable("homeScreen") { HomeScreen(navController) }
                composable("browseScreen") { BrowseScreen(cardViewModel, navController) }
                composable("favoritesScreen") {
                    FavoritesScreen(navController, cardUiState = cardViewModel.cardUiState)
                }
                composable("filterBar"){ SearchFilter() }
            }
        }
    }
}

@Composable
fun Title(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        color = Color.White,
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.transparent_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Search Bar
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            )

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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),  // This will make the Row take up the full width of the screen
                horizontalArrangement = Arrangement.Center  // This centers its children horizontally
            ) {
                // Browse Bar Box
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(16.dp)
                        .height(50.dp)
                        .background(Color(0xFFFFA500))  // Orange color
                        .clickable { navController.navigate("browseScreen") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Browse",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),  // This will make the Row take up the full width of the screen
                horizontalArrangement = Arrangement.Center  // This centers its children horizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(16.dp)
                        .height(50.dp)
                        .background(Color(0xFFFFA500))  // Orange color
                        .clickable {
                            navController.navigate("favoritesScreen")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View favorites",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blurred),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // or ContentScale.FillBounds as needed
                )
                // Bottom Tab Bar in a Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp) // Set the height of the Row
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically //
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
}


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val navController = rememberNavController() // Create a mock NavController
//    MTGCreatureSearchTheme {
//        HomeScreen(navController = navController, cardUiState = cardViewModel.cardUiState) // Pass the NavController
//    }
//}
