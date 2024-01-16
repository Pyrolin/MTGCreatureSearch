package com.example.mtgcreaturesearch.View

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.R
import com.example.mtgcreaturesearch.ViewModel.CardViewModel

@Composable
fun CardScreen(cardViewModel: CardViewModel = viewModel(), navController: NavController, card: ShownCards, flipped: Boolean = false) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.card_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.backspace),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Transparent)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                    if (card.card_faces?.size == 2) {
                        Spacer(modifier = Modifier.weight(1f))
                        //turn_card not implemented yet
                        Image(painter = painterResource(id = if (flipped) R.drawable.turn_card_active else R.drawable.turn_card),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color.Transparent)
                                .clickable {
                                    navController.popBackStack()
                                    navController.navigate("cardScreen/${card.id}?flipped=${!flipped}")
                                }
                        )
                    }
                }
            }
            Card(cardViewModel, navController, card, setonClick = false, flipped = flipped)
        }
        BottomBar(navController = navController, cardViewModel = cardViewModel, modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(55.dp),
            backgroundImage = R.drawable.card_blurred)
    }

}