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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        "Toughness" to listOf("Option A", "Option B", "Option C"),
        "Power" to listOf("Option X", "Option Y", "Option Z"),
        "Mana cost" to listOf("Option Red", "Option Blue", "Option Green")
    )

    LazyColumn {
        items(data) { (name, options) ->
            CardSet(name = name, options = options)
        }
    }
}

@Composable
fun MagicCards() {
    Column {
        Box(modifier = Modifier.padding(16.dp)) {
            FilterBar()
        }
        CardList()
    }
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun MagicCardsPreview() {
    MagicCards()
}
