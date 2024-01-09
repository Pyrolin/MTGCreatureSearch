package com.example.mtgcreaturesearch.ViewModel

import CardApi
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.ShownCards
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.launch
import java.io.IOException

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
            } catch (e: IOException) {
                CardUiState.Error
            }
        }
    }

    var filteredCardUiState: CardUiState by mutableStateOf(CardUiState.Loading)
        private set

    fun getFilteredCards(url: String) {
        viewModelScope.launch {
            filteredCardUiState = try {
                val listResult = CardApi.retrofitService.getPhotos(url).data
                CardUiState.Success(listResult)
            } catch (e: IOException) {
                CardUiState.Error
            }
        }
    }

    fun getQuery(
        mana: Int? = null,
        toughness: Int? = null,
        power: Int? = null,
        swamp: Boolean = false,
        plains: Boolean = false,
        island: Boolean = false,
        mountain: Boolean = false,
        forest: Boolean = false,
    ): String {

        var basequery = "search?order=name&q=type%3Acreature"

        if (swamp) {
            basequery += "+color%3DB"
        }

        if (plains) {
            basequery += "+color%3DW"
        }

        if (island) {
            basequery += "+color%3DU"
        }

        if (mountain) {
            basequery += "+color%3DR"
        }

        if (forest) {
            basequery += "+color%3DG"
        }

        basequery += "+%28game%3Apaper%29"

        if (mana != null) {
            basequery += "+mana%3D$mana"
        }

        if (toughness != null) {
            basequery += "+tou%3D$toughness"
        }

        if (power != null) {
            basequery += "+pow%3D$power"
        }
        return basequery
    }

    fun browseCards(): List<ShownCards> {
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState = cardUiState) {
            is CardUiState.Success -> {
                val cards = mutableListOf<ShownCards>()
                for (i in 0 until currentState.photos.size) {
                    val photo = currentState.photos[i]
                    if (photo.layout == "transform") {
                        val card = photo.card_faces?.get(0)?.image_uris?.let {
                            ShownCards(
                                it.small,
                                photo.id
                            )
                        }
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

    fun filteredCards(url: String): List<ShownCards> {
        getFilteredCards(url)
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState = filteredCardUiState) {
            is CardUiState.Success -> {
                val cards = mutableListOf<ShownCards>()
                for (i in 0 until currentState.photos.size) {
                    val photo = currentState.photos[i]
                    if (photo.layout == "transform") {
                        val card = photo.card_faces?.get(0)?.image_uris?.let {
                            ShownCards(
                                it.small,
                                photo.id
                            )
                        }
                        if (card != null) {
                            cards.add(card)
                            cards
                        }
                    } else {
                        val card = photo.image_uris?.let { ShownCards(it.small, photo.id) }
                        if (card != null) {
                            cards.add(card)
                            cards
                        }
                    }
                }
                cards
            }

            else -> {
                return emptyList()
            }
        }
    }

    fun favoriteCards(): List<ShownCards> {
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState = cardUiState) {
            is CardUiState.Success -> {
                val cards = mutableListOf<ShownCards>()
                for (i in 0 until currentState.photos.size) {
                    val photo = currentState.photos[i]
                    if (photo.layout == "transform") {
                        val card = photo.card_faces?.get(0)?.image_uris?.let {
                            ShownCards(
                                it.small,
                                photo.id
                            )
                        }
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

                    if (favorites.contains(card.id)) favorites.remove(card.id) else favorites.add(
                        card.id
                    )
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
