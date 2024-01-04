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
                for (i in 0 until currentState.photos.size){
                    val photo = currentState.photos[i]
                    if (photo.layout=="transform"){
                        val card = photo.card_faces?.get(0)?.image_uris?.let { ShownCards(it.small,photo.id) }
                        if (card != null) {
                            cards.add(card)
//                            println(card.url)
                            cards
                        }
                    } else {
                        val card = photo.image_uris?.let { ShownCards(it.small, photo.id) }
                        if (card != null) {
                            cards.add(card)
//                            println(card.url)
                            cards
                        }
                    }
//                    println(cards[60].url)
//                    println(currentState.photos[60].image_uris?.small)
//                    println(currentState.photos[60].card_faces?.get(0)?.image_uris?.small)
//                    if(cards[i].url != currentState.photos[i].image_uris?.small && cards[i].url !=currentState.photos[i].card_faces?.get(0)?.image_uris?.small ){
//                        println(cards[i].url)
//                        println(currentState.photos[i].image_uris?.small)
//                        println(currentState.photos[i].card_faces?.get(0)?.image_uris?.small)
//                    }
                }
//                println(currentState.photos[52].image_uris?.small)
//                println(currentState.photos[52].card_faces?.get(0)?.image_uris?.small)
                println(currentState.photos.size)
//                println(currentState.photos[0])
//                println(currentState.photos[174])
                println(cards.size)
//                println(cards[173].url)
                cards
            }
            else -> {
                return emptyList()
            }
        }
    }


}