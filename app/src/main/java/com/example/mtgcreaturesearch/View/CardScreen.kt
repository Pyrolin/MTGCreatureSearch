package com.example.mtgcreaturesearch.View

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.ViewModel.CardViewModel

@Composable
fun CardScreen(cardViewModel: CardViewModel = viewModel(), navController: NavController, card: ShownCards) {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(cardViewModel, navController, card, setonClick = false)
    }
}