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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.View.BottomBar
import com.example.mtgcreaturesearch.View.Title
import com.example.mtgcreaturesearch.View.queryString
import com.example.mtgcreaturesearch.ViewModel.CardViewModel

var mana: String = ""
var toughness: String = ""
var power: String = ""
var swamp: Boolean = false
var plains: Boolean = false
var island: Boolean = false
var mountain: Boolean = false
var forest: Boolean = false
var textSearch: String = ""

var tmp_mana: String = ""
var tmp_toughness: String = ""
var tmp_power: String = ""
var tmp_swamp: Boolean = false
var tmp_plains: Boolean = false
var tmp_island: Boolean = false
var tmp_mountain: Boolean = false
var tmp_forest: Boolean = false

// Drop down menus
@Composable
fun CardSet(cardViewModel: CardViewModel = viewModel(), name: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    if (name == "Toughness") {
        selectedOption = toughness
    } else if (name == "Power") {
        selectedOption = power
    } else if (name == "Mana cost") {
        selectedOption = mana
    }

    // Card class
    Card(modifier = Modifier.padding(16.dp)) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter $name",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                if (selectedOption != null) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = selectedOption ?: "",
                        modifier = Modifier.padding(end = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = option
                                expanded = false

                                if (name == "Toughness") {
                                    tmp_toughness = option
                                }

                                if (name == "Power") {
                                    tmp_power = option
                                }

                                if (name == "Mana cost") {
                                    tmp_mana = option
                                }
                            }
                        ) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}

// Drop down options
@Composable
fun CardList() {
    val data = listOf(
        "Toughness" to listOf("","0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16+"),
        "Power" to listOf("","0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16+"),
        "Mana cost" to listOf("","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "X")
    )
    var disabled = floatArrayOf(
        0.5f, 0f, 0f, 0f, -50f,
        0f, 0.5f, 0f, 0f, -50f,
        0f, 0f, 0.5f, 0f, -50f,
        0f, 0f, 0f, 1f, 0f
    )
    val enabled = floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )
    val imageIds = listOf(
        R.drawable.swamp,
        R.drawable.plains,
        R.drawable.island,
        R.drawable.mountain,
        R.drawable.forest
    )

    val clickStates = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            imageIds.forEach {
                put(
                    it,
                    if (it == R.drawable.swamp) {
                        tmp_swamp
                    } else if (it == R.drawable.plains) {
                        tmp_plains
                    } else if (it == R.drawable.island) {
                        tmp_island
                    } else if (it == R.drawable.mountain) {
                        tmp_mountain
                    } else if (it == R.drawable.forest) {
                        tmp_forest
                    } else {
                        false
                    }
                )
            }
        }
    }

    LazyColumn {
        items(data) { (name, options) ->
            CardSet(name = name, options = options)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                imageIds.forEach { imageId ->
                    Box(
                        modifier = Modifier
                            .width(54.dp)
                            .height(54.dp)
                            .clickable {
                                clickStates[imageId] = !clickStates.getValue(imageId)
                                if (imageId == R.drawable.swamp) {
                                    tmp_swamp = (clickStates[imageId] == true)
                                }
                                if (imageId == R.drawable.plains) {
                                    tmp_plains = (clickStates[imageId] == true)
                                }
                                if (imageId == R.drawable.island) {
                                    tmp_island = (clickStates[imageId] == true)
                                }
                                if (imageId == R.drawable.mountain) {
                                    tmp_mountain = (clickStates[imageId] == true)
                                }
                                if (imageId == R.drawable.forest) {
                                    tmp_forest = (clickStates[imageId] == true)
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .background(Color.Transparent, CircleShape),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(
                                (if (clickStates[imageId] == true) {
                                    ColorMatrix(enabled)
                                } else {
                                    ColorMatrix(disabled)
                                })
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun filterSearch(
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf(textSearch) }

    androidx.compose.material3.TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            textSearch = newValue
            searchQuery = newValue
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
        ),
        // Reset textbox button
        trailingIcon = {
            IconButton(onClick = {
                searchQuery = ""
                textSearch = ""
            }) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.reset_text),
                    contentDescription = "Clear text"
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        placeholder = {
            androidx.compose.material3.Text(text = "Search Textbox...")
        }
    )
}

// Screen
@Composable
fun SearchFilter(
    cardViewModel: CardViewModel,
    navController: NavController,
    startDestination: String
) {
    tmp_mana = mana
    tmp_toughness = toughness
    tmp_power = power
    tmp_swamp = swamp
    tmp_plains = plains
    tmp_island = island
    tmp_mountain = mountain
    tmp_forest = forest

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.filter_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Back button
                Image(
                    painter = painterResource(id = R.drawable.backspace),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                        .clickable {
                            if (startDestination == "browseScreen") {
                                cardViewModel.getQuery(
                                    mana,
                                    toughness,
                                    power,
                                    swamp,
                                    plains,
                                    island,
                                    mountain,
                                    forest,
                                    queryString,
                                    textSearch
                                )

                                navController.navigate("browseScreen")
                            } else {
                                navController.navigate("favoritesScreen?cmc=${mana}&toughness=${toughness}&power=${power}&swamp=${swamp}&plains=${plains}&island=${island}&mountain=${mountain}&forest=${forest}&text=${textSearch}")
                            }
                        }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Refresh page button
                Image(
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                        .clickable {
                            mana = ""
                            toughness = ""
                            power = ""
                            swamp = false
                            plains = false
                            island = false
                            mountain = false
                            forest = false
                            textSearch = ""
                            navController.navigate("filterBar/${startDestination}")
                        }
                )
            }

            filterSearch()

            CardList()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {

                // Search button
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable {
                            mana = tmp_mana
                            toughness = tmp_toughness
                            power = tmp_power
                            swamp = tmp_swamp
                            plains = tmp_plains
                            island = tmp_island
                            mountain = tmp_mountain
                            forest = tmp_forest

                            if (startDestination == "browseScreen") {
                                cardViewModel.getQuery(
                                    mana,
                                    toughness,
                                    power,
                                    swamp,
                                    plains,
                                    island,
                                    mountain,
                                    forest,
                                    queryString,
                                    textSearch
                                )

                                navController.navigate("browseScreen")

                            } else {
                                navController.navigate("favoritesScreen?cmc=${mana}&toughness=${toughness}&power=${power}&swamp=${swamp}&plains=${plains}&island=${island}&mountain=${mountain}&forest=${forest}&text=${textSearch}")
                            }


                        }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            BottomBar(
                navController = navController, cardViewModel = cardViewModel, modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                R.drawable.filter_bottom_background
            )
        }
    }
}
