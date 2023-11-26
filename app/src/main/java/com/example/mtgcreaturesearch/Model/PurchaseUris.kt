package com.example.mtgcreaturesearch.Model
import kotlinx.serialization.Serializable


@Serializable
data class PurchaseUris(
    val cardhoarder: String,
    val cardmarket: String,
    val tcgplayer: String
)