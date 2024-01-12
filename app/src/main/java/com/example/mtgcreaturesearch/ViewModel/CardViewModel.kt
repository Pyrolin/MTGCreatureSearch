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
import com.example.mtgcreaturesearch.Model.Query
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.View.queryString
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

//Filler

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore
val favorites_collection = db.collection("favorites")

var favorites: MutableList<ShownCards> =  mutableListOf()

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
        getCardPhotos(Query("", ""))
    }

    /**
     * Gets Card data from the ScryfallAPI Retrofit service.
     */
    fun getCardPhotos(query: Query) {
        viewModelScope.launch {
            cardUiState = try {
                val order = if (query.order.isNotEmpty()) query.order else "name"
                val q = if (query.q.isNotEmpty()) query.q else "type%3Acreature+%28game%3Apaper%29"

                val listResult = CardApi.retrofitService.getPhotos(order, q).data
                CardUiState.Success(listResult)
            } catch (e: HttpException) {
                e.response()?.errorBody()?.string().let {
                    if (it != null) {
                        Log.e("HTTP ERROR", it)
                    }
                }
                e.response().toString().let { Log.e("HTTP ERROR", it) }

                CardUiState.Error
            }
        }
    }

    fun getQuery(
        mana: String = "",
        toughness: String = "",
        power: String = "",
        swamp: Boolean = false,
        plains: Boolean = false,
        island: Boolean = false,
        mountain: Boolean = false,
        forest: Boolean = false,
        search: String = "",
        text: String = ""
    ): Query {

        val order = "name"
        var q = ""
        if (search.isNotEmpty()) {
            q += search
            if (text.isNotEmpty()) {
                q += if (text.contains(" ")) {
                    "+%28oracle%3A${text.replace(" ", " oracle%3A")})"
                } else {
                    "+oracle%3A${text}"
                }
            }
            q += "+type%3Acreature+%28game%3Apaper%29"
        }
        else {
            if (text.isNotEmpty()) {
                q += if (text.contains(" ")) {
                    "%28oracle%3A${text.replace(" ", " oracle%3A")})"
                } else {
                    "oracle%3A${text}"
                }

                q += "+type%3Acreature+%28game%3Apaper%29"
            } else {
                q += "type%3Acreature+%28game%3Apaper%29"
            }
        }


        var hasColor = false;

        if (swamp) {
            q += "+color%3DB"
            hasColor = true
        }

        if (plains) {
            if (hasColor) {
                q += "W"
            } else {
                q += "+color%3DW"
            }
            hasColor = true
        }

        if (island) {
            if (hasColor) {
                q += "U"
            } else {
                q += "+color%3DU"
            }
            hasColor = true
        }

        if (mountain) {
            if (hasColor) {
                q += "R"
            } else {
                q += "+color%3DR"
            }
            hasColor = true
        }

        if (forest) {
            if (hasColor) {
                q += "G"
            } else {
                q += "+color%3DG"
            }
            hasColor = true
        }

        if (mana.isNotEmpty()) {
            q += "+cmc%3D$mana"
        }

        if (toughness.isNotEmpty()) {
            q += "+tou%3D$toughness"
        }

        if (power.isNotEmpty()) {
            q += "+pow%3D$power"
        }

        Log.d("QUERY", q)

        return Query(order,q)
    }

    fun browseCards(query: Query): List<ShownCards> {
        getCardPhotos(query)
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState = cardUiState) {
            is CardUiState.Success -> {
                val cards = mutableListOf<ShownCards>()
                for (i in 0 until currentState.photos.size) {
                    val photo = currentState.photos[i]
                    if (photo.layout == "transform") {
                        val card = photo.card_faces?.get(0)?.image_uris?.let {
                            ShownCards(
                                it.large,
                                photo.id
                            )
                        }
                        if (card != null) {
                            cards.add(card)
//                            println(card.url)
                            cards
                        }
                    } else {
                        val card = photo.image_uris?.let { ShownCards(it.large, photo.id) }
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


    fun favoriteCards(): List<ShownCards> {
        return favorites
    }

    fun getCardFromID(cardID: String): ShownCards {
            //val cards: MutableList<ShownCards> = mutableListOf()
            return when (val currentState=cardUiState){
                is CardUiState.Success ->{
                    for (i in 0 until currentState.photos.size){
                        if(currentState.photos[i].id == cardID) {
                            if (currentState.photos[i].layout=="transform"){
                                return currentState.photos[i].card_faces?.get(0)?.image_uris?.let { ShownCards(it.large, currentState.photos[i].id) }!!

                            } else {
                                return currentState.photos[i].image_uris?.let { ShownCards(it.large, currentState.photos[i].id) }!!
                            }
                        }
                    }
                    return getFavoritedFromID(cardID)
                }
                else -> {
                    return getFavoritedFromID(cardID)
                }
            }
        }

    fun initFavorites() {
        favorites_collection.document(devideID).get().addOnSuccessListener { document ->
            if (document != null) {
                if (document.data?.get("favorites") != null) {
                    val fetchedFavorites = document.data?.get("favorites") as MutableList<HashMap<String, String>>

                    favorites = makeFavoritesList(fetchedFavorites)
                }
            }
        }
    }

    fun makeFavoritesList(cardMapList: MutableList<HashMap<String, String>>): MutableList<ShownCards> {
        val cardList: MutableList<ShownCards> = mutableListOf()

        cardMapList.forEach { it ->
            var id = ""
            var url = ""
            it.forEach { (key, value) ->
                if (key == "id") {
                    id = value
                } else if (key == "url") {
                    url = value
                }
            }
            cardList.add(ShownCards(url, id))
        }

        return cardList
    }

    fun makeFirebaseList(cardList: MutableList<ShownCards>): MutableList<HashMap<String, String>> {
        val cardMapList: MutableList<HashMap<String, String>> = mutableListOf()

        cardList.forEach { card ->
            val data = hashMapOf(
                "id" to card.id,
                "url" to card.url
            )
            cardMapList.add(data)
        }

        return cardMapList
    }

    fun updateFavorites(card: ShownCards) {
        if (isFavorited(card)) removeFavorited(card) else addFavorited(card)

        val data = hashMapOf(
            "favorites" to makeFirebaseList(favorites),
        )

        favorites_collection.document(devideID).set(data)
    }

    fun isFavorited(card: ShownCards): Boolean {
        for (favoriteCard in favorites) {
            if (favoriteCard.id == card.id) {
                return true
            }
        }
        return false
    }

    fun removeFavorited(card: ShownCards) {
        for (favoriteCard in favorites) {
            if (favoriteCard.id == card.id) {
                favorites.remove(favoriteCard)
                return
            }
        }
    }
    fun addFavorited(card: ShownCards) {
        favorites.add(card)
    }

    fun getFavoritedFromID(cardID: String): ShownCards {
        for (favoriteCard in favorites) {
            if (favoriteCard.id == cardID) {
                return favoriteCard
            }
        }
        return ShownCards("", "")
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
