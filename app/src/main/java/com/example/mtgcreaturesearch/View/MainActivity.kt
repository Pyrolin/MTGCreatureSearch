package com.example.mtgcreaturesearch.View

import SearchFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navArgument
import com.example.mtgcreaturesearch.Model.Query
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

var queryString = ""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cardViewModel: CardViewModel = viewModel()

            cardViewModel.initDevice()

            NavHost(navController, startDestination = "homeScreen") {
                composable("homeScreen") { HomeScreen(cardViewModel ,navController) }
                composable(
                    route = "browseScreen?order={order}&q={q}",
                    arguments = listOf(navArgument("order") {defaultValue = "name"},
                        navArgument("q") {defaultValue = "type%3Acreature+%28game%3Apaper%29"}
                    )) {
                        backStackEntry ->
                    val order = backStackEntry.arguments?.getString("order")
                    val q = backStackEntry.arguments?.getString("q")
                    if (order != null) {
                        if (q != null) {
                            BrowseScreen(cardViewModel, navController, order, q)
                        }
                    }
                }
                composable(
                    route = "favoritesScreen?cmc={cmc}&toughness={toughness}&power={power}&swamp={swamp}&plains={plains}&island={island}&mountain={mountain}&forest={forest}&text={text}",
                    arguments = listOf(navArgument("cmc") {defaultValue = "0.0"},
                        navArgument("toughness") {defaultValue = ""},
                        navArgument("power") {defaultValue = ""},
                        navArgument("swamp") {defaultValue = "false"},
                        navArgument("plains") {defaultValue = "false"},
                        navArgument("island") {defaultValue = "false"},
                        navArgument("mountain") {defaultValue = "false"},
                        navArgument("forest") {defaultValue = "false"},
                        navArgument("text") {defaultValue = ""},

                        )) {
                        backStackEntry ->
                    val cmc = backStackEntry.arguments?.getString("cmc")
                    val toughness = backStackEntry.arguments?.getString("toughness")
                    val power = backStackEntry.arguments?.getString("power")
                    val swamp = backStackEntry.arguments?.getString("swamp")
                    val plains = backStackEntry.arguments?.getString("plains")
                    val island = backStackEntry.arguments?.getString("island")
                    val mountain = backStackEntry.arguments?.getString("mountain")
                    val forest = backStackEntry.arguments?.getString("forest")
                    val text = backStackEntry.arguments?.getString("text")

                    val filter = cardViewModel.getFavoriteQuery(cmc, toughness, power, swamp, plains, island, mountain, forest, text)

                    FavoritesScreen(cardViewModel, navController, filter)
                }
                composable(
                    route  = "filterBar/{startDestination}",
                    arguments = listOf(navArgument("startDestination") { type = NavType.StringType })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("startDestination")
                        ?.let { SearchFilter(cardViewModel, navController, it) }
                }
                composable(
                    route = "cardScreen/{cardID}",
                    arguments = listOf(navArgument("cardID") { type = NavType.StringType })
                ) {backStackEntry ->
                    backStackEntry.arguments?.getString("cardID")
                        ?.let { CardScreen(cardViewModel, navController, cardViewModel.getCardFromID(it)) }
                }
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
fun SearchBar(
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    reloadPage: Boolean = false
) {
    var searchQuery by remember { mutableStateOf(queryString) }

    TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            queryString = newValue
            searchQuery = newValue
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone ={
                val query = cardViewModel.getQuery(mana, toughness, power, swamp, plains, island, mountain, forest, queryString, textSearch)
                navController.navigate("browseScreen?order=${query.order}&q=${query.q}")
            }
        ),
        trailingIcon = {
            IconButton(onClick = {
                searchQuery = ""
                queryString = ""
                if (reloadPage) {
                    val query = cardViewModel.getQuery(mana, toughness, power, swamp, plains, island, mountain, forest, queryString, textSearch)
                    navController.navigate("browseScreen?order=${query.order}&q=${query.q}")
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.reset_text),
                    contentDescription = "Clear text"
                )
            }
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
fun BottomBar(navController: NavController, cardViewModel: CardViewModel, modifier: Modifier, backgroundImage: Int) {
    // Bottom Tab Bar
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
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
                        val query = cardViewModel.getQuery(
                            mana,
                            toughness,
                            power,
                            swamp,
                            plains,
                            island,
                            mountain,
                            forest,
                            queryString
                        )
                        navController.navigate("browseScreen?order=${query.order}&q=${query.q}")
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.favorite_hvid),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Transparent)
                    .clickable {
                        navController.navigate("favoritesScreen?cmc=${mana}&toughness=${toughness}&power=${power}&swamp=${swamp}&plains=${plains}&island=${island}&mountain=${mountain}&forest=${forest}&text=${textSearch}")
                    }
            )
        }
    }
}
    @Composable
        fun CardRow(cards: List<ShownCards>, navController: NavController) {
            when (cards.size > 3) {
                true -> {
                    val startCard = (0..cards.size-3).random()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cards.subList(startCard, startCard+3)) { card ->
                            Card(navController = navController, card = card)
                        }
                    }
                }

                else -> {}
            }

        }

        @Composable
        fun HomeScreen(cardViewModel: CardViewModel = viewModel(), navController: NavController) {
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
                        cardViewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                        navController,
                    )

                    CardRow(cards = cardViewModel.browseCards(Query("", "")), navController = navController)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        // Browse Bar Box
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .padding(16.dp)
                                .height(50.dp)
                                .background(Color(0xFFFFA500))  // Orange color
                                .clickable {
                                    val query = cardViewModel.getQuery(mana, toughness, power, swamp, plains, island, mountain, forest, queryString)
                                    navController.navigate("browseScreen?order=${query.order}&q=${query.q}")
                                },
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
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .padding(16.dp)
                                .height(50.dp)
                                .background(Color(0xFFFFA500))
                                .clickable {
                                    navController.navigate("favoritesScreen?cmc=${mana}&toughness=${toughness}&power=${power}&swamp=${swamp}&plains=${plains}&island=${island}&mountain=${mountain}&forest=${forest}&text=${textSearch}")
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

                    BottomBar(navController = navController, cardViewModel = cardViewModel, modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                        R.drawable.blurred)
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
