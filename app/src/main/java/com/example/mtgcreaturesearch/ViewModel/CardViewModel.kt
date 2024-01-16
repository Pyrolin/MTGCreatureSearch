package com.example.mtgcreaturesearch.ViewModel

import CardApi
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.mtgcreaturesearch.Model.CardFace
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.ImageUrisX
import com.example.mtgcreaturesearch.Model.Query
import com.example.mtgcreaturesearch.Model.ShownCards
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.Serializable
import java.util.Locale

//Filler

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore
val favorites_collection = db.collection("favorites")

var favorites: MutableList<ShownCards> =  mutableListOf()

var devideID = ""

var testQuery = Query("name", "type%3Acreature+%28game%3Apaper%29")

sealed interface CardUiState {
    data class Success(val data: List<Data>) : CardUiState
    object Error : CardUiState
    object Loading : CardUiState
}
open class CardViewModel : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var cardUiState: CardUiState by mutableStateOf(CardUiState.Loading)
        private set


    /**
     * Gets Card data from the ScryfallAPI Retrofit service.
     */
    fun getCardPhotos(query: Query) {
        viewModelScope.launch {
            cardUiState = try {
                val order = if (query.order.isNotEmpty()) query.order else "name"
                val q = if (query.q.isNotEmpty()) query.q else "type%3Acreature+%28game%3Apaper%29"
                Log.d("API CALL", query.page.toString())
                val listResult = CardApi.retrofitService.getPhotos(order, q, query.page.toString()).data
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
            if (toughness == "16+") {
                q += "+tou%3E%3D16"
            } else {
                q += "+tou%3D$toughness"
            }
        }

        if (power.isNotEmpty()) {
            if (power == "16+") {
                q += "+pow%3E%3D16"
            } else {
                q += "+pow%3D$power"
            }
        }

        return Query(order,q)
    }

    fun getFavoriteQuery(
        cmc: String? = "-1.0",
        toughness: String? = "",
        power: String? = "",
        swamp: String? = "false",
        plains: String? = "false",
        island: String? = "false",
        mountain: String? = "false",
        forest: String? = "false",
        text: String? = ""
    ): ShownCards {

        var cmcDouble = -1.0
        if (cmc != null) {
            if (cmc.isNotEmpty()) {
                if (cmc == "X") {
                    cmcDouble = 0.0
                } else {
                    cmcDouble = cmc?.toDouble()!!
                }
            }
        }
        val swampBoolean = swamp.toBoolean()
        val plainsBoolean = plains.toBoolean()
        val islandBoolean = island.toBoolean()
        val mountainBoolean = mountain.toBoolean()
        val forestBoolean = forest.toBoolean()

        val colors = mutableListOf<String>()

        if (swampBoolean) {
            colors.add("B")
        }

        if (plainsBoolean) {
            colors.add("W")
        }

        if (islandBoolean) {
            colors.add("U")
        }

        if (mountainBoolean) {
            colors.add("R")
        }

        if (forestBoolean) {
            colors.add("G")
        }

        return ShownCards("","",toughness,power,cmcDouble,"",colors,text, mutableListOf())
    }

    fun getCards(page: Int): List<ShownCards> {
        return browseCards(testQuery, page)
    }

    fun browseCards(query: Query, page: Int = 1): List<ShownCards> {
        Log.d("TEST", "test")
        getCardPhotos(Query(query.order, query.q, page))
        //val cards: MutableList<ShownCards> = mutableListOf()
        return when (val currentState = cardUiState) {
            is CardUiState.Success -> {
                val cards = mutableListOf<ShownCards>()
                for (i in 0 until currentState.data.size) {
                    val data = currentState.data[i]
                    if (data.layout == "transform") {
                        val card = data.card_faces?.get(0)?.image_uris?.let {
                            ShownCards(
                                it.large,
                                data.id,
                                data.toughness,
                                data.power,
                                data.cmc,
                                data.layout,
                                data.colors,
                                data.oracle_text,
                                data.card_faces
                            )
                        }
                        if (card != null) {
                            cards.add(card)
//                            println(card.url)
                            cards
                        }
                    } else {
                        val card = data.image_uris?.let {
                                ShownCards(
                                    it.large,
                                    data.id,
                                    data.toughness,
                                    data.power,
                                    data.cmc,
                                    data.layout,
                                    data.colors,
                                    data.oracle_text,
                                    data.card_faces
                                )
                        }
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
                println(currentState.data.size)
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


    fun favoriteCards(filter: ShownCards): List<ShownCards> {

        val filteredCards: MutableList<ShownCards> = mutableListOf()

        for (card in favorites) {
            var hasColors = true
            var hasText = true
            var hasToughness = false
            var hasPower = false

            if (card.card_faces?.size == 2) {

                for (color in filter.colors!!) {
                    var foundColor = true
                    for (face in card.card_faces!!) {
                        if ((face.colors != null) && !face.colors.contains(color) && !foundColor) {
                            hasColors = false
                        } else if ((face.colors == null) || !face.colors.contains(color)) {
                            foundColor = false
                        } else if ((face.colors == null) && !foundColor) {
                            hasColors = false
                        }
                    }
                }

                val textList: List<String>? = filter.oracle_text?.split(" ")?.map { it.trim() }

                if (filter.oracle_text!!.isNotEmpty()) {
                    for (word in textList!!) {
                        var foundText = true
                        for (face in card.card_faces!!) {
                            if ((face.oracle_text != null) && !face.oracle_text.lowercase(Locale.ROOT).contains(word) && !foundText) {
                                hasText = false
                            } else if ((face.oracle_text == null) || !card.oracle_text?.lowercase(Locale.ROOT)?.contains(word)!!) {
                                foundText = false
                            } else if ((face.oracle_text == null) && !foundText) {
                                hasText = false
                            }
                        }
                    }
                }

                if (filter.toughness?.isNotEmpty() == true) {
                    for (face in card.card_faces!!) {
                        if ((face.toughness != null) && face.toughness == filter.toughness || (face.toughness?.toIntOrNull() == null && filter.toughness == "0") || (face.toughness?.toIntOrNull() != null && filter.toughness == "16+" && face.toughness.toInt() >= 16) || (face.power == "∞" && filter.power == "16+")) {
                            hasToughness = true
                        }
                    }
                } else {
                    hasToughness = true
                }

                if (filter.power?.isNotEmpty() == true) {
                    for (face in card.card_faces!!) {
                        if ((face.power != null) && face.power == filter.power || (face.power?.toIntOrNull() == null && filter.power == "0") || (face.power?.toIntOrNull() != null && filter.power == "16+" && face.power.toInt() >= 16) || (face.power == "∞" && filter.power == "16+")) {
                            hasPower = true
                        }
                    }
                } else {
                    hasPower = true
                }

            } else {
                for (color in filter.colors!!) {
                    if (!card.colors?.contains(color)!!) {
                        hasColors = false
                        break
                    }
                }

                val textList: List<String>? = filter.oracle_text?.split(" ")?.map { it.trim() }

                if (filter.oracle_text!!.isNotEmpty()) {
                    for (word in textList!!) {
                        if (!card.oracle_text?.lowercase(Locale.ROOT)?.contains(word)!!) {
                            hasText = false
                            break
                        }
                    }
                }

                hasToughness = (filter.toughness?.isEmpty() == true || card.toughness == filter.toughness)

                if (filter.toughness == "16+" && !hasToughness && card.toughness?.toIntOrNull() != null) {
                    hasToughness = (card.toughness?.toInt()!! >= 16)
                } else if ((card.toughness?.toIntOrNull() == null && filter.toughness == "0") || (card.power == "∞" && filter.power == "16+")) {
                    hasToughness = true
                }

                hasPower = (filter.power?.isEmpty() == true || card.power == filter.power)


                if (filter.power == "16+" && !hasPower && card.power?.toIntOrNull() != null) {
                    hasPower = (card.power?.toInt()!! >= 16)
                } else if ((card.power?.toIntOrNull() == null && filter.power == "0") || (card.power == "∞" && filter.power == "16+")) {
                    hasPower = true
                }
            }

            if (hasToughness && hasPower && (filter.cmc == -1.0 || card.cmc == filter.cmc) && hasColors && hasText) {
                filteredCards.add(card)
            }
        }

        return filteredCards
    }

    fun getCardFromID(cardID: String): ShownCards {
            //val cards: MutableList<ShownCards> = mutableListOf()
            return when (val currentState=cardUiState){
                is CardUiState.Success ->{
                    for (i in 0 until currentState.data.size){
                        if(currentState.data[i].id == cardID) {
                            val data = currentState.data[i]
                            if (data.layout=="transform"){
                                return data.card_faces?.get(0)?.image_uris?.let {
                                    ShownCards(it.large,
                                        data.id,
                                        data.toughness,
                                        data.power,
                                        data.cmc,
                                        data.layout,
                                        data.colors,
                                        data.oracle_text,
                                        data.card_faces
                                    )
                                }!!

                            } else {
                                return data.image_uris?.let {
                                    ShownCards(
                                        it.large,
                                        data.id,
                                        data.toughness,
                                        data.power,
                                        data.cmc,
                                        data.layout,
                                        data.colors,
                                        data.oracle_text,
                                        data.card_faces
                                    )
                                }!!
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
                    val fetchedFavorites = document.data?.get("favorites") as MutableList<HashMap<String, Any>>

                    favorites = makeFavoritesList(fetchedFavorites)
                }
            }
        }
    }

    fun makeFavoritesList(cardMapList: MutableList<HashMap<String, Any>>): MutableList<ShownCards> {
        val cardList: MutableList<ShownCards> = mutableListOf()

        cardMapList.forEach { it ->
            var id = ""
            var url = ""
            var toughness = ""
            var power = ""
            var cmc = 0.0
            var layout = ""
            var colors = mutableListOf<String>()
            var oracle_text = ""
            var card_faces = mutableListOf<CardFace>()

            it.forEach { (key, value) ->
                if (key == "id") {
                    id = value.toString()
                } else if (key == "url") {
                    url = value.toString()
                } else if (key == "toughness") {
                    toughness = value.toString()
                } else if (key == "power") {
                    power = value.toString()
                } else if (key == "cmc") {
                    cmc = value as Double
                } else if (key == "layout") {
                    layout = value.toString()
                } else if (key == "colors") {
                    colors = value as MutableList<String>
                } else if (key == "oracle_text") {
                    oracle_text = value.toString()
                } else if (key == "card_faces") {
                    val value_faces = value as MutableList<HashMap<String, Any?>>

                    Log.d("TEST", layout)

                    value_faces.forEach { face ->
                        card_faces.add(CardFace(
                            image_uris = face["large"]
                            ?.let { it1 -> face["png"]
                                ?.let { it2 -> face["small"]
                                    ?.let { it3 -> ImageUrisX(it1 as String, it2 as String, it3 as String) } } },
                            colors = if (face["colors"] == null) mutableListOf() else face["colors"] as MutableList<String>,
                            oracle_text = face["oracle_text"] as String,
                            power =  face["power"] as String,
                            toughness = face["toughness"] as String
                        ))
                    }
                }
            }



            cardList.add(ShownCards(url, id, toughness, power, cmc, layout, colors, oracle_text, card_faces))
        }

        return cardList
    }

    fun makeFirebaseList(cardList: MutableList<ShownCards>): MutableList<HashMap<String, Any?>> {
        val cardMapList: MutableList<HashMap<String, Any?>> = mutableListOf()

        cardList.forEach { card ->
            val card_faces: MutableList<HashMap<String, Any?>> = mutableListOf()

            card.card_faces?.forEach { card_face ->
                val images = hashMapOf(
                    "large" to if (card.layout == "flip") card.url else card_face.image_uris?.large,
                    "png" to card_face.image_uris?.png,
                    "small" to card_face.image_uris?.small
                )

                val card_face_map = hashMapOf(
                    "colors" to card_face.colors,
                    "image_uris" to images,
                    "oracle_text" to (card_face.oracle_text ?: ""),
                    "power" to (card_face.power ?: ""),
                    "toughness" to (card_face.toughness ?: "")
                )

                card_faces.add(card_face_map)
            }

            val data: HashMap<String, Any?>

            if (card.card_faces?.size == 2) {
                data = hashMapOf(
                    "id" to card.id,
                    "url" to card.url,
                    "toughness" to "",
                    "power" to "",
                    "cmc" to card.cmc,
                    "layout" to card.layout,
                    "colors" to mutableListOf<String>(),
                    "oracle_text" to "",
                    "card_faces" to card_faces
                )
            } else {
                data = hashMapOf(
                    "id" to card.id,
                    "url" to card.url,
                    "toughness" to card.toughness,
                    "power" to card.power,
                    "cmc" to card.cmc,
                    "layout" to card.cmc,
                    "colors" to card.colors,
                    "oracle_text" to card.oracle_text,
                    "card_faces" to card_faces
                )
            }

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
        return ShownCards("", "", "", "", 0.0, "", mutableListOf(), "", mutableListOf())
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
