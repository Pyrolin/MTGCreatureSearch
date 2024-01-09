package com.example.mtgcreaturesearch.ViewModel

import CardPagingSource
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.ShownCards
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.installations.FirebaseInstallations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

//Filler

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore
val favorites_collection = db.collection("favorites")

var favorites: MutableList<String> =  mutableListOf()

var devideID = ""

sealed interface CardUiState {
    data class Success(val photos: List<Data>) : CardUiState
    object Error : CardUiState
    object Loading : CardUiState
}
@HiltViewModel
class CardViewModel @Inject constructor(
    private val cardPagingSource: CardPagingSource
) : ViewModel() {
    private val _cardResponse: MutableStateFlow<PagingData<Data>> =
        MutableStateFlow(PagingData.empty())
    var cardResponse = _cardResponse.asStateFlow()
        private set

    init {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    175, enablePlaceholders = true
                )
            ) {
                cardPagingSource
            }.flow.cachedIn(viewModelScope).collect {
                _cardResponse.value = it
            }
        }
    }



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
                val listResult = CardApi.retrofitService.getPhotos(0).data
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

    fun favoriteCards(): List<ShownCards>{
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState=cardUiState){
            is CardUiState.Success ->{
                val cards = mutableListOf<ShownCards> ()
                for (i in 0 until currentState.photos.size){
                    val photo = currentState.photos[i]
                    if (photo.layout=="transform"){
                        val card = photo.card_faces?.get(0)?.image_uris?.let { ShownCards(it.small,photo.id) }
                        if (card != null && favorites.contains(card.id)) {
                            cards.add(card)
//                            println(card.url)
                            cards
                        }
                    } else {
                        val card = photo.image_uris?.let { ShownCards(it.small, photo.id) }
                        if (card != null && favorites.contains(card.id)) {
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
//                println(cards[52].url)
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

    fun initFavorites() {
            favorites_collection.document(devideID).get().addOnSuccessListener { document ->
                if (document != null) {
                    if (document.data?.get("favorites") != null) {
                        favorites = document.data?.get("favorites") as MutableList<String>
                    }
                }
            }
    }

    fun updateFavorites(card: ShownCards) {
        favorites_collection.document(devideID).get().addOnSuccessListener { document ->
            if (document != null) {
                if (document.data?.get("favorites") != null) {
                    favorites = document.data?.get("favorites") as MutableList<String>

                    if (favorites.contains(card.id)) favorites.remove(card.id) else favorites.add(card.id)
                } else {
                    favorites.add(card.id)
                }

                val data = hashMapOf(
                    "favorites" to favorites,
                )

                favorites_collection.document(devideID).set(data)
            }
        }
    }

    fun isFavorited(card: ShownCards): Boolean {
        return favorites.contains(card.id);
    }

    fun initDevice() {
        // [START get_installation_id]
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Installations", "Installation ID: " + task.result)
                devideID = task.result
                initFavorites()
            } else {
                Log.e("Installations", "Unable to get Installation ID")
            }
        }
        // [END get_installation_id]
    }


}