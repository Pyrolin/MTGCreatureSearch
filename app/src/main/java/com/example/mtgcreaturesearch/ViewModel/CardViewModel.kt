package com.example.mtgcreaturesearch.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.ShownCards
import kotlinx.coroutines.launch
import java.io.IOException

//Filler

sealed interface CardUiState {
    data class Success(val photos: List<Data>) : CardUiState
    object Error : CardUiState
    object Loading : CardUiState
}
class CardViewModel : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var cardUiState: CardUiState by mutableStateOf(CardUiState.Loading)
        private set

    /**
     * Call getCardPhotos() on init so we can display status immediately.
     */
    init {
        getCardPhotos()
    }

    /**
     * Gets Card data from the ScryfallAPI Retrofit service.
     */
    fun getCardPhotos() {
        viewModelScope.launch {
            cardUiState = try {
                val listResult = CardApi.retrofitService.getPhotos().data
                CardUiState.Success(listResult)
            } catch (e:IOException) {
                CardUiState.Error
            }
        }
    }

    fun browseCards(): List<ShownCards>{
        //val cards: MutableList<ShownCards> = mutableListOf()
            return when (val currentState=cardUiState){
            is CardUiState.Success ->{
                val cards = mutableListOf<ShownCards> ()
                for (i in 0.. currentState.photos.size-1){
                    val photo = currentState.photos[i]
                    val card = ShownCards(photo.image_uris.small, photo.id)
                    cards.add(card)
                }
                cards
            }
            else -> {
                return emptyList()
            }
        }
    }


}