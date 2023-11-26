package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class RelatedUris(
    val edhrec: String,
    val gatherer: String,
    val tcgplayer_infinite_articles: String,
    val tcgplayer_infinite_decks: String
)