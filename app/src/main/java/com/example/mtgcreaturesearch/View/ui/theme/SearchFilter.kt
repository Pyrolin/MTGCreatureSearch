import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
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
        label = { Text("Seacrh") },
        leadingIcon = { Icon(Icons.Filled.Search, "search") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { /* filter logic here */ })
    )
}

@Preview
@Composable
fun FilterBarPreview() {
    FilterBar()
}

@Composable
fun CardSet(index: Int) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Set",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp,)
                )
            }
        }
    }
}
@Composable
fun CardList() {
    LazyColumn {
        items(10) { index ->
            CardSet(index = index)
        }
    }
}

@Composable
fun MagicCards(){
    Column {
        Box(modifier = Modifier.padding(16.dp)) {
            FilterBar()
        }
        CardList()
    }
}

@Preview
@Composable
fun MagicCardsPrewiev() {
    MagicCards()
}








