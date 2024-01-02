import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.View.Title

@Composable
fun FilterBar() {
    val filterText = remember { mutableStateOf("") }

    TextField(
        value = filterText.value,
        onValueChange = { newText ->
            filterText.value = newText
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Search") },
        leadingIcon = { Icon(Icons.Filled.Search, "search") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { /* filter logic here */ })
    )
}

@Composable
fun CardSet(name: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.firstOrNull()) }

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

                if (expanded) {
                    Spacer(modifier = Modifier.weight(1f)) // Add a spacer to push the selected option to the right
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


@Composable
fun CardList() {
    val data = listOf(
        "Set" to listOf("Option 1", "Option 2", "Option 3"),
        "Toughness" to listOf("1", "2", "3", "4", "5", "6", "7", "8"),
        "Power" to listOf("1", "2", "3", "4", "5", "6", "7", "8"),
        "Mana cost" to listOf("1", "2", "3", "4", "5", "6", "7", "8")
    )

    LazyColumn {
        items(data) { (name, options) ->
            CardSet(name = name, options = options)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly, // Adjust the arrangement as needed
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.swamp),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.plains),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.island),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.mountain),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.forest),
                    contentDescription = null,
                    modifier = Modifier
                        .width(54.dp)
                        .height(54.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}




@Composable
fun SearchFilter() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.filter_background), // Replace with your background image resource ID
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

            Box(modifier = Modifier.padding(16.dp)) {
                FilterBar()
            }
            CardList()
        }
    }
}


@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun MagicCardsPreview() {
    SearchFilter()
}
