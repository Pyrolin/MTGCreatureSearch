import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.R
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

                                if(name == "Mana cost") {
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
        "Set" to listOf("","Option 1", "Option 2", "Option 3"),
        "Toughness" to listOf("","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16+"),
        "Power" to listOf("","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16+"),
        "Mana cost" to listOf("","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "X")
    )

    val imageIds = listOf(R.drawable.swamp, R.drawable.plains, R.drawable.island, R.drawable.mountain, R.drawable.forest)

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
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = null,
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
                            .background(if (clickStates[imageId] == true) Color(0xFFFFA500) else Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

//Actual composable
@Composable
fun SearchFilter(cardViewModel: CardViewModel, navController: NavController) {
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
                Image(
                    painter = painterResource(id = R.drawable.backspace),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
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
                            navController.navigate("filterBar")
                        }
                )
            }
            CardList()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFFFA500))
                        .clickable {
                            mana = tmp_mana
                            toughness = tmp_toughness
                            power = tmp_power
                            swamp = tmp_swamp
                            plains = tmp_plains
                            island = tmp_island
                            mountain = tmp_mountain
                            forest = tmp_forest
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
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.filter_bottom_background),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
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

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun MagicCardsPreview() {
}
