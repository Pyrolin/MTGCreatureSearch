package com.example.mtgcreaturesearch.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun CardScreen(cardViewModel: CardViewModel = viewModel(), navController: NavController, card: ShownCards) {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(cardViewModel, navController, card, setonClick = false)
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
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
                //turn_card not implemented yet
                Image(painter = painterResource(id = R.drawable.turn_card),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Transparent)
                        .clickable {
                            navController.popBackStack()
                        }
                    )
            }
        }
        BottomBar(navController = navController, cardViewModel = cardViewModel, modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(55.dp),
            backgroundImage = R.drawable.black_bar)
    }

}